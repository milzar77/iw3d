package com.blogspot.fravalle.iw3d.jme.sources;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import java.util.List;

public interface IRenderingSource {

    public List<Iw3dInternetNode> applyMatrix(Object sourceData, AssetManager assetManager, Node nUniverse3d, EViewerType etype);

    public void addMatrix(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> list, EViewerType etype);

}
