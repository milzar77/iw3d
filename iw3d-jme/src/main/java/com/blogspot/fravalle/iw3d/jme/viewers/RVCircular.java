package com.blogspot.fravalle.iw3d.jme.viewers;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

public class RVCircular extends ARenderingViewer {

    public RVCircular() {}

    @Override
    public void visualizzaDati(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> parList) {
        List<Iw3dInternetNode> list = parList;
        hm = new LinkedHashMap<String, Vector<Iw3dInternetNode>>();

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

        nUniverse3d.attachChild(this.createNodeForMatrix("NetOrigin", assetManager, list.get(domainOriginIndex),0F,false, 0.1f, 16, ColorRGBA.Yellow));

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
                n.attachChild(this.createNodeForMatrix("Origin::"+dom.getIwid(), assetManager, dom, moveByIncrement+=moveIncrement, false, 0.1f, 3, ColorRGBA.White));
            }
            Quaternion pitch = new Quaternion();
            pitch.fromAngleAxis((FastMath.PI * (increment+=incrementDeg)) / 180F, new Vector3f(0,0,1));
            n.setLocalRotation(pitch);
            //System.out.println("ROTATING BY DEGREE:"+increment+" EQUIVALENT TO RADIANT: " + ((FastMath.PI * increment) / 180));
            nUniverse3d.attachChild(n);
        }

    }

    @Override
    public Node createNodeForMatrix(String originId, AssetManager assetManager, Iw3dInternetNode node, Float moveByIncrement, boolean isDummy, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
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
            Material matStar = new Material(assetManager, /*"Common/MatDefs/Misc/Unshaded.j3md"*/"Common/MatDefs/Light/Lighting.j3md");
            //matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data

            matStar.setFloat("Shininess", 15f);
            matStar.setBoolean("UseMaterialColors", true);
            matStar.setColor("Ambient", ColorRGBA.Yellow.mult(0.2f));
            matStar.setColor("Diffuse", ColorRGBA.Yellow.mult(0.2f));
            matStar.setColor("Specular", ColorRGBA.Yellow.mult(0.8f));


            geomDomain.setMaterial(matStar);

            //FIX: this.fillWithUserData(geomDomain, hygimport);

            this.fillWithUserData(geomDomain, node);

            domainOrigin.attachChild(geomDomain);
        }

        if (!originId.equals("NetOrigin")) {
            domainOrigin.move(new Vector3f(0F, moveByIncrement, 0F));
        } else {
            domainOrigin.move(new Vector3f(0F, 0F, 0F));
        }

        return domainOrigin;
    }
}
