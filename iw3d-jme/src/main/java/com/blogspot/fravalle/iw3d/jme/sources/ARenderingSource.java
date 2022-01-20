package com.blogspot.fravalle.iw3d.jme.sources;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import java.util.List;

abstract public class ARenderingSource implements IRenderingSource {

    private ARenderingViewer viewer;

    //protected List<Iw3dInternetNode> list;

    @Override
    public void addMatrix(AssetManager assetManager, Node nUniverse3d, List<Iw3dInternetNode> list, EViewerType etype) {

        nUniverse3d.detachAllChildren();

        switch (etype) {
            case CIRCULAR:
                System.out.println("GOT IT CIRCULAR");
                viewer = new RVCircular();
                break;
            case QUAD:
                System.out.println("GOT IT QUAD");
                viewer = new RVQuad();
                break;
            case WEB:
                System.out.println("GOT IT WEB");
                viewer = new RVWeb();
                break;
            default:
                ;
        }

        if (viewer!=null) {
            viewer.visualizzaDati(assetManager, nUniverse3d, list);
            viewer.traverseSceneForConnection(assetManager, nUniverse3d);
        }

    }


}
