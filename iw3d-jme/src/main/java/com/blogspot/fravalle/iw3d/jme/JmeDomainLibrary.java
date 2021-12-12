package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.MyDataLoader;
import com.blogspot.fravalle.data.google.GoogleSearchImporter;
import com.blogspot.fravalle.data.orm.derby.cayenne.iw3d.Iwebipv4;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import org.apache.cayenne.Cayenne;
import org.lwjgl.Sys;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class JmeDomainLibrary {

    private static JmeDomainLibrary instance;

    private Random rand = new Random(1000);

    private List<Iwebipv4> list;

    private JmeDomainLibrary() {

    }

    public static JmeDomainLibrary getInstance() {
        if (instance==null) {
            instance = new JmeDomainLibrary();
        }
        return instance;
    }

    public void addSurfingCircularMatrix(String sUrl, AssetManager assetManager, Node nUniverse3d) {
        //TODO: get url
        DataConfiguration.staccaSessione();
        GoogleSearchImporter google = new GoogleSearchImporter();
        this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
        System.out.println("URLs:\n"+list);
        this.addCircularMatrix(assetManager, nUniverse3d);
    }

    public void addBookmarkImportCircularMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.list = MyDataLoader.getInstance().getDomains(false);
        this.addCircularMatrix(assetManager, nUniverse3d);
    }

    private void addCircularMatrix(AssetManager assetManager, Node nUniverse3d) {

        HashMap<String, Vector<Iwebipv4>> hm = new LinkedHashMap<String, Vector<Iwebipv4>>();

        for (Iwebipv4 dom : list) {
            String keyId = String.valueOf(dom.getIwcategoryid());
            Vector<Iwebipv4> v = hm.get(keyId);
            if (v!=null) {
                v.add(dom);
            } else {
                v = new Vector<>();
                v.add(dom);
            }
            hm.put(keyId, v);
        }

        int domainOriginIndex = 0;

        nUniverse3d.attachChild(this.createNodeForCircularMatrix("NetOrigin", 0F,false, assetManager, list.get(domainOriginIndex), 0.1f, 16, ColorRGBA.Yellow));

        list.remove(domainOriginIndex);

        float increment=0F;
        // 360 / hm.size = ANGLE INCREMENT PERIOD
        float incrementDeg=360F/hm.size();
        //360 : hm.size = ANGLE : i ==> ANGLE = (360*i)/hm.size
        for (String categoryKey : hm.keySet()) {
            Node n = new Node("Category_" + categoryKey);
            Vector<Iwebipv4> v = hm.get(categoryKey);
            Float moveByIncrement = 0F;
            Float moveIncrement = 2F;
            for (Iwebipv4 dom : v) {
                n.attachChild(this.createNodeForCircularMatrix("Origin::"+dom.getObjectId(), moveByIncrement+=moveIncrement, false, assetManager, dom, 0.1f, 3, ColorRGBA.White));
            }
            Quaternion pitch = new Quaternion();
            pitch.fromAngleAxis((FastMath.PI * (increment+=incrementDeg)) / 180F, new Vector3f(0,0,1));
            n.setLocalRotation(pitch);
            //System.out.println("ROTATING BY DEGREE:"+increment+" EQUIVALENT TO RADIANT: " + ((FastMath.PI * increment) / 180));
            nUniverse3d.attachChild(n);
        }

    }

    public void addSurfingWebMatrix(String sUrl, AssetManager assetManager, Node nUniverse3d) {
        //TODO: get url
        DataConfiguration.staccaSessione();
        GoogleSearchImporter google = new GoogleSearchImporter();
        this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
        System.out.println("URLs:\n"+list);
        this.addWebMatrix(assetManager, nUniverse3d);
    }

    public void addBookmarkImportWebMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.list = MyDataLoader.getInstance().getDomains(false);
        this.addWebMatrix(assetManager, nUniverse3d);
    }

    public void addWebMatrix(AssetManager assetManager, Node nUniverse3d) {

        HashMap<String, Vector<Iwebipv4>> hm = new LinkedHashMap<String, Vector<Iwebipv4>>();

        for (Iwebipv4 dom : list) {
            String keyId = String.valueOf(dom.getIwcategoryid());
            Vector<Iwebipv4> v = hm.get(keyId);
            if (v!=null) {
                v.add(dom);
            } else {
                v = new Vector<>();
                v.add(dom);
            }
            hm.put(keyId, v);
        }

        int domainOriginIndex = 0;

        nUniverse3d.attachChild(this.createNodeForWebMatrix("NetOrigin", false, assetManager, list.get(domainOriginIndex), 0.1f, 16, ColorRGBA.Yellow));

        list.remove(domainOriginIndex);

        float increment=0F;
        // 360 / hm.size = ANGLE INCREMENT PERIOD
        float incrementDeg=360F/hm.size();
        //360 : hm.size = ANGLE : i ==> ANGLE = (360*i)/hm.size
        for (String categoryKey : hm.keySet()) {
            Node n = new Node("Category_" + categoryKey);
            Vector<Iwebipv4> v = hm.get(categoryKey);
            Float moveByIncrement = 0F;
            Float moveIncrement = 2F;
            for (Iwebipv4 dom : v) {
                n.attachChild(this.createNodeForWebMatrix("Origin::"+dom.getObjectId(), false, assetManager, dom, 0.1f, 3, ColorRGBA.White));
            }
            Quaternion pitch = new Quaternion();
            pitch.fromAngleAxis((FastMath.PI * (increment+=incrementDeg)) / 180F, new Vector3f(0,0,1));
            n.setLocalRotation(pitch);
            //System.out.println("ROTATING BY DEGREE:"+increment+" EQUIVALENT TO RADIANT: " + ((FastMath.PI * increment) / 180));
            nUniverse3d.attachChild(n);
        }

    }

    private Node createNodeForWebMatrix(String originId, boolean isDummy, AssetManager assetManager, Iwebipv4 hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            this.fillWithUserData(geomDomain, hygimport);

            domainOrigin.attachChild(geomDomain);
        }

        if (!originId.equals("NetOrigin")) {
            Float fY = rand.nextFloat() * 75F;
            domainOrigin.move(new Vector3f(0F, fY, 0F));
        } else {
            domainOrigin.move(new Vector3f(0F, 0F, 0F));
        }

        return domainOrigin;
    }

    private Node createNodeForCircularMatrix(String originId, Float moveByIncrement, boolean isDummy, AssetManager assetManager, Iwebipv4 hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            this.fillWithUserData(geomDomain, hygimport);

            domainOrigin.attachChild(geomDomain);
        }

        if (!originId.equals("NetOrigin")) {
            domainOrigin.move(new Vector3f(0F, moveByIncrement, 0F));
        } else {
            domainOrigin.move(new Vector3f(0F, 0F, 0F));
        }

        return domainOrigin;
    }

    public void addSurfingHorizontalMatrix(String sUrl, AssetManager assetManager, Node nUniverse3d) {
        //TODO: get url
        DataConfiguration.staccaSessione();
        GoogleSearchImporter google = new GoogleSearchImporter();
        this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
        System.out.println("URLs:\n"+list);
        this.addHorizontalMatrix(assetManager, nUniverse3d);
    }

    public void addBookmarkImportHorizontalMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.list = MyDataLoader.getInstance().getDomains(false);
        this.addHorizontalMatrix(assetManager, nUniverse3d);
    }

    private void addHorizontalMatrix(AssetManager assetManager, Node nUniverse3d) {

        int starOriginIndex = 0;

        nUniverse3d.attachChild(this.createNodeForHorizontalMatrix("NetOrigin", false, assetManager, list.get(starOriginIndex), 0.1f, 16, ColorRGBA.Yellow));

        list.remove(starOriginIndex);

        for (Iwebipv4 hygimport : list) {
            nUniverse3d.attachChild(this.createNodeForHorizontalMatrix("Origin::"+hygimport.getObjectId(),false, assetManager, hygimport, 0.1f, 3, ColorRGBA.White));
        }
    }

    private Node createNodeForHorizontalMatrix(String originId, boolean isDummy, AssetManager assetManager, Iwebipv4 hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+Cayenne.pkForObject(hygimport).toString(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            this.fillWithUserData(geomDomain, hygimport);

            domainOrigin.attachChild(geomDomain);
        }

        if (!originId.equals("NetOrigin")) {
            domainOrigin.move(new Vector3f(hygimport.getIwcategoryid(), rand.nextFloat() * 10, 0F));
        } else {
            domainOrigin.move(new Vector3f(0F, 0F, 0F));
        }

        return domainOrigin;
    }
/*
    public void addFilteredStars(AssetManager assetManager, Node nUniverse3d, ColumnToSearch[] columns) {
        // TODO: sistemare problema derivante da update del loop 3D
        nUniverse3d.detachAllChildren();
        // TODO: load filtered stars
        this.list = MyDataLoader.getInstance().getStarsByColumns(columns, false);

        int starOriginIndex = 0;

        nUniverse3d.attachChild(this.createStar("SunOrigin", false, assetManager, list.get(starOriginIndex), 1f, 16, ColorRGBA.Yellow));

        list.remove(starOriginIndex);

        for (Iwebipv4 hygimport : list) {
            nUniverse3d.attachChild(this.createStar("Origin::"+hygimport.getObjectId(),false, assetManager, hygimport, 0.1f, 3, ColorRGBA.White));
        }
    }
*/
    public void starSelection(AssetManager assetManager, Node nUniverse3d, List<Iwebipv4> starNeighbours) {

        //reset color
        for (Spatial spatial : nUniverse3d.getChildren()) {
            Geometry geom = (Geometry) ((Node) spatial).getChildren().get(0);
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            if (!spatial.getName().equals("SunOrigin")) {
                matStar.setColor("Color", ColorRGBA.White);//TODO: implement color star detector from spectral star data
            } else {
                matStar.setColor("Color", ColorRGBA.Yellow);//TODO: implement color star detector from spectral star data
            }
            geom.setMaterial(matStar);
        }
        //set selection color
        for (Iwebipv4 hyg : starNeighbours) {
            for (Spatial spatial : nUniverse3d.getChildren()) {
                Geometry geom = (Geometry) ((Node) spatial).getChildren().get(0);
                String idName = "domainId_" + Cayenne.pkForObject(hyg).toString();
                if (!idName.equals("domainId_0")&&geom.getName().equals(idName)) {
                    Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    matStar.setColor("Color", ColorRGBA.Orange);//TODO: implement color star detector from spectral star data
                    geom.setMaterial(matStar);
                }
            }
        }
    }

    private void fillWithUserData(Geometry geomDomain, Iwebipv4 hygimport) {
        geomDomain.setUserData("domainId", Integer.valueOf(Cayenne.pkForObject(hygimport).toString()));
        geomDomain.setUserData("domainHostName", hygimport.getIwdomainname());
        geomDomain.setUserData("domainHostPath", hygimport.getIwurl());
        geomDomain.setUserData("domainCategory", hygimport.getIwcategoryname());
    }

}
