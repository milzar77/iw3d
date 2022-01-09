package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.MyDataLoader;
import com.blogspot.fravalle.data.webspider.WebspiderScannerNosqlImport;
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

import java.util.*;

public class JmeDomainLibrary {

    private static JmeDomainLibrary instance;

    private Random rand = new Random(1000);

    private List<Iw3dInternetNode> list;


    private JmeDomainLibrary() {
        this.list = new ArrayList<Iw3dInternetNode>();
    }

    public static JmeDomainLibrary getInstance() {
        if (instance==null) {
            instance = new JmeDomainLibrary();
        }
        return instance;
    }

    public void applySurfingCircularMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.addCircularMatrix(assetManager, nUniverse3d);
    }

    public void applySurfingHorizontalMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.addHorizontalMatrix(assetManager, nUniverse3d);
    }

    public void applySurfingRandomWebMatrix(AssetManager assetManager, Node nUniverse3d) {
        this.addWebMatrix(assetManager, nUniverse3d);
    }

    public void addSurfingCircularMatrix(String sUrl, AssetManager assetManager, Node nUniverse3d) {
        //TODO: get url
        DataConfiguration.staccaSessione();

        if (DataConfiguration.useDynamoDb()) {
            System.out.println("URLs:\n"+list);
        }/* else {
            WebspiderScannerSqlImport google = new WebspiderScannerSqlImport(sUrl, false);
            google.crawlUrls(true);
            google.importData();
            this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
            //System.out.println("URLs:\n"+list);
        }*/

        if (!this.list.isEmpty())
            this.addCircularMatrix(assetManager, nUniverse3d);
        else
            System.err.println("No data loaded");
    }

    public void addBookmarkImportCircularMatrix(AssetManager assetManager, Node nUniverse3d) {
        //this.list = MyDataLoader.getInstance().getDomains(false);
        this.list = Iw3dInternetNode.query( "client-"+DataConfiguration.SESSION_ID.toString() );
        if (!this.list.isEmpty())
            this.addCircularMatrix(assetManager, nUniverse3d);
        else
            System.err.println("No data loaded");
    }

    private void addCircularMatrix(AssetManager assetManager, Node nUniverse3d) {

        HashMap<String, Vector<Iw3dInternetNode>> hm = new LinkedHashMap<String, Vector<Iw3dInternetNode>>();

        for (Iw3dInternetNode dom : list) {
            String keyId = String.valueOf(dom.getIwcategoryid());
            Vector<Iw3dInternetNode> v = hm.get(keyId);
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
            Vector<Iw3dInternetNode> v = hm.get(categoryKey);
            Float moveByIncrement = 0F;
            Float moveIncrement = 2F;
            for (Iw3dInternetNode dom : v) {
                n.attachChild(this.createNodeForCircularMatrix("Origin::"+dom.getIwid(), moveByIncrement+=moveIncrement, false, assetManager, dom, 0.1f, 3, ColorRGBA.White));
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
        if (DataConfiguration.useDynamoDb()) {
            WebspiderScannerNosqlImport google = new WebspiderScannerNosqlImport(sUrl, false);
            google.crawlUrls(true);
            //this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
            this.list = Iw3dInternetNode.query( "client-"+DataConfiguration.SESSION_ID.toString() );
            if (!this.list.isEmpty())
                this.addWebMatrix(assetManager, nUniverse3d);
            else
                System.err.println("No data loaded");
        } /*else {
            WebspiderScannerSqlImport google = new WebspiderScannerSqlImport(sUrl, false);
            google.crawlUrls(true);
            google.importData();
            this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
            if (!this.list.isEmpty())
                this.addWebMatrix(assetManager, nUniverse3d);
            else
                System.err.println("No data loaded");
        }*/
        //System.out.println("URLs:\n"+list);
        //TODO: debug
        //if (true) return;


    }

    public void addBookmarkImportWebMatrix(AssetManager assetManager, Node nUniverse3d) {
        //this.list = MyDataLoader.getInstance().getDomains(false);
        this.list = Iw3dInternetNode.query( "client-"+DataConfiguration.SESSION_ID.toString() );
        if (!this.list.isEmpty())
            this.addWebMatrix(assetManager, nUniverse3d);
        else
            System.err.println("No data loaded");
    }

    public void addWebMatrix(AssetManager assetManager, Node nUniverse3d) {

        HashMap<String, Vector<Iw3dInternetNode>> hm = new LinkedHashMap<String, Vector<Iw3dInternetNode>>();

        for (Iw3dInternetNode dom : list) {
            String keyId = String.valueOf(dom.getIwcategoryid());
            Vector<Iw3dInternetNode> v = hm.get(keyId);
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
            Vector<Iw3dInternetNode> v = hm.get(categoryKey);
            Float moveByIncrement = 0F;
            Float moveIncrement = 2F;
            for (Iw3dInternetNode dom : v) {
                n.attachChild(this.createNodeForWebMatrix("Origin::"+dom.getIwid(), false, assetManager, dom, 0.1f, 3, ColorRGBA.White));
            }
            Quaternion pitch = new Quaternion();
            pitch.fromAngleAxis((FastMath.PI * (increment+=incrementDeg)) / 180F, new Vector3f(0,0,1));
            n.setLocalRotation(pitch);
            //System.out.println("ROTATING BY DEGREE:"+increment+" EQUIVALENT TO RADIANT: " + ((FastMath.PI * increment) / 180));
            nUniverse3d.attachChild(n);
        }

    }

    private Node createNodeForWebMatrix(String originId, boolean isDummy, AssetManager assetManager, Iw3dInternetNode node, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+node.getIwid(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+node.getIwid(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            this.fillWithUserData(geomDomain, node);

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

    private Node createNodeForCircularMatrix(String originId, Float moveByIncrement, boolean isDummy, AssetManager assetManager, Iw3dInternetNode hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+hygimport.getIwid(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+hygimport.getIwid(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            //FIX: this.fillWithUserData(geomDomain, hygimport);

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
        if (DataConfiguration.useDynamoDb()) {
            System.out.println("URLs:\n"+list);
        }/* else {
            WebspiderScannerSqlImport google = new WebspiderScannerSqlImport(sUrl, false);
            this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
        }*/
        //System.out.println("URLs:\n"+list);
        if (!this.list.isEmpty())
            this.addHorizontalMatrix(assetManager, nUniverse3d);
        else
            System.err.println("No data loaded");
    }

    public void addBookmarkImportHorizontalMatrix(AssetManager assetManager, Node nUniverse3d) {
        //this.list = MyDataLoader.getInstance().getDomains(false);
        this.list = Iw3dInternetNode.query( "client-"+DataConfiguration.SESSION_ID.toString() );
        if (!this.list.isEmpty())
            this.addHorizontalMatrix(assetManager, nUniverse3d);
        else
            System.err.println("No data loaded");
    }

    private void addHorizontalMatrix(AssetManager assetManager, Node nUniverse3d) {

        int starOriginIndex = 0;

        nUniverse3d.attachChild(this.createNodeForHorizontalMatrix("NetOrigin", false, assetManager, list.get(starOriginIndex), 0.1f, 16, ColorRGBA.Yellow));

        list.remove(starOriginIndex);

        for (Iw3dInternetNode hygimport : list) {
            nUniverse3d.attachChild(this.createNodeForHorizontalMatrix("Origin::"+hygimport.getIwid(),false, assetManager, hygimport, 0.1f, 3, ColorRGBA.White));
        }
    }

    private Node createNodeForHorizontalMatrix(String originId, boolean isDummy, AssetManager assetManager, Iw3dInternetNode hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+hygimport.getIwid(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+hygimport.getIwid(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            //FIX: this.fillWithUserData(geomDomain, hygimport);

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
    public void starSelection(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> starNeighbours) {

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
        for (Iw3dInternetNode hyg : starNeighbours) {
            for (Spatial spatial : nUniverse3d.getChildren()) {
                Geometry geom = (Geometry) ((Node) spatial).getChildren().get(0);
                String idName = "domainId_" + hyg.getIwid();
                if (!idName.equals("domainId_0")&&geom.getName().equals(idName)) {
                    Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    matStar.setColor("Color", ColorRGBA.Orange);//TODO: implement color star detector from spectral star data
                    geom.setMaterial(matStar);
                }
            }
        }
    }

    private void fillWithUserData(Geometry geomDomain, Iw3dInternetNode node) {
        geomDomain.setUserData("domainId", node.getIwid());
        geomDomain.setUserData("domainHostName", node.getIwdomainname());
        geomDomain.setUserData("domainHostPath", node.getIwurl());
        //FIX: geomDomain.setUserData("domainCategory", node.getIwcategoryname());
    }

}
