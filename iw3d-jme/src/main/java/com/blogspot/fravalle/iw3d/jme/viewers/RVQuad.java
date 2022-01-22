package com.blogspot.fravalle.iw3d.jme.viewers;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import java.util.List;

public class RVQuad extends ARenderingViewer {

    public RVQuad() {}

    @Override
    public void visualizzaDati(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> list) {
        int starOriginIndex = 0;

        nUniverse3d.attachChild(this.createNodeForMatrix("NetOrigin", assetManager, list.get(starOriginIndex), 0f, false, 0.1f, 16, ColorRGBA.Yellow));

        list.remove(starOriginIndex);

        for (Iw3dInternetNode hygimport : list) {
            nUniverse3d.attachChild(this.createNodeForMatrix("Origin::"+hygimport.getIwid(), assetManager, hygimport, 0f, false, 0.1f, 3, ColorRGBA.White));
        }
    }

    @Override
    public Node createNodeForMatrix(String originId, AssetManager assetManager, Iw3dInternetNode iw3dInternetNode, Float moveByIncrement, boolean isDummy, float defaultRadius, int defaultSamples, ColorRGBA defaultColor) {
        Node domainOrigin = new Node(originId);
        Geometry geomDomain = null;

        //System.err.println("pkForObject="+Cayenne.pkForObject(hygimport).toString());

        if (isDummy) {
            Box bStar = new Box(defaultRadius, defaultRadius, defaultRadius);
            // TODO: use primary key id as geometry name
            geomDomain = new Geometry("domainId_"+iw3dInternetNode.getIwid(), bStar);
        } else {
            Sphere sStar = new Sphere(defaultSamples, defaultSamples, defaultRadius);
            geomDomain = new Geometry("domainId_"+iw3dInternetNode.getIwid(), sStar);
        }

        if (geomDomain != null) {
            Material matStar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            matStar.setColor("Color", defaultColor);//TODO: implement color star detector from spectral star data
            geomDomain.setMaterial(matStar);

            //FIX: this.fillWithUserData(geomDomain, hygimport);

            this.fillWithUserData(geomDomain, iw3dInternetNode);

            domainOrigin.attachChild(geomDomain);
        }

        if (!originId.equals("NetOrigin")) {
            domainOrigin.move(new Vector3f(iw3dInternetNode.getIwcategoryid(), rand.nextFloat() * 10, 0F));
        } else {
            domainOrigin.move(new Vector3f(0F, 0F, 0F));
        }

        return domainOrigin;
    }
}
