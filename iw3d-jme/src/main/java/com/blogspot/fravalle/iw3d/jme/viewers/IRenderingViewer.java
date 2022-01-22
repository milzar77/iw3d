package com.blogspot.fravalle.iw3d.jme.viewers;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import java.util.List;

public interface IRenderingViewer {

    public void visualizzaDati(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> list);

    //public void createNodeForMatrix(String originId, AssetManager assetManager, Iw3dInternetNode node, boolean isDummy, float defaultRadius, int defaultSamples, ColorRGBA defaultColor);
    public Node createNodeForMatrix(String originId, AssetManager assetManager, Iw3dInternetNode iw3dInternetNode, Float moveByIncrement, boolean isDummy, float defaultRadius, int defaultSamples, ColorRGBA defaultColor);

    public void traverseSceneForConnection(AssetManager assetManager, Node nUniverse3d);

}
