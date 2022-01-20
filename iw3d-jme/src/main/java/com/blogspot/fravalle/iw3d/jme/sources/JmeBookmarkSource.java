package com.blogspot.fravalle.iw3d.jme.sources;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.iw3d.jme.simpleapplication.WindowJme3DSimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Analisi dei bookmark
 */
public class JmeBookmarkSource extends ARenderingSource {

    @Override
    public List<Iw3dInternetNode> applyMatrix(Object sourceData, AssetManager assetManager, Node nUniverse3d, EViewerType etype) {

        List<Iw3dInternetNode> list = new ArrayList<>();

        WindowJme3DSimpleApplication.creaSessione();

        //FIX in caso di NON surfing:
        DataConfiguration.SESSION_ID = DataConfiguration.STARTING_SESSION_ID;

        list = Iw3dInternetNode.query( "client-"+ DataConfiguration.SESSION_ID.toString() );

        if (!list.isEmpty()) {
            super.addMatrix(assetManager, nUniverse3d, list, etype);
        } else
            System.err.println("No data loaded");

        return list;

    }


}
