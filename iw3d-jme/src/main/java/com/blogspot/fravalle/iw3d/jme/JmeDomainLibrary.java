package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.data.ColumnToSearch;
import com.blogspot.fravalle.data.MyDataLoader;
import com.blogspot.fravalle.data.orm.derby.cayenne.iw3d.Iwebipv4;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import org.apache.cayenne.Cayenne;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

public class JmeDomainLibrary {

    private static JmeDomainLibrary instance;

    private Random rand = new Random(10);

    private List<Iwebipv4> list;

    private JmeDomainLibrary() {

    }

    public static JmeDomainLibrary getInstance() {
        if (instance==null) {
            instance = new JmeDomainLibrary();
        }
        return instance;
    }

    public void addUniverseStars(AssetManager assetManager, Node nUniverse3d) {
        //this.list = DataLoader.getInstance().getStars();
        BigDecimal bigDec = new BigDecimal(10.0D);
        this.list = MyDataLoader.getInstance().getDomains();

        int starOriginIndex = 0;

        nUniverse3d.attachChild(this.createStar("NetOrigin", false, assetManager, list.get(starOriginIndex), 0.1f, 16, ColorRGBA.Yellow));

        list.remove(starOriginIndex);

        for (Iwebipv4 hygimport : list) {
            nUniverse3d.attachChild(this.createStar("Origin::"+hygimport.getObjectId(),false, assetManager, hygimport, 0.1f, 3, ColorRGBA.White));
        }
    }

    private Node createStar(String originId, boolean isDummy, AssetManager assetManager, Iwebipv4 hygimport, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
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

            geomDomain.setUserData("domainId", Integer.valueOf(Cayenne.pkForObject(hygimport).toString()));
            geomDomain.setUserData("domainName", hygimport.getIwdomainname());
            geomDomain.setUserData("domainCategory", hygimport.getIwcategoryname());
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
}
