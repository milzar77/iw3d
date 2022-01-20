package com.blogspot.fravalle.iw3d.jme.sources;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.webspider.WebspiderScannerNosqlImport;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggiungere disposizione spaziale in base a parametri cronologici, dimensionali, http code e capire cosa altro si può categorizzare a livello di risorsa su protocollo http
 */
public class JmeSpiderSource extends ARenderingSource {

    @Override
    public List<Iw3dInternetNode> applyMatrix(Object sourceData, AssetManager assetManager, Node nUniverse3d, EViewerType etype) {

        List<Iw3dInternetNode> list = new ArrayList<>();

        String sUrl = String.valueOf(sourceData);

        DataConfiguration.staccaSessione();

        if (DataConfiguration.useDynamoDb()) {
            //FIX: nUniverse3d.detachAllChildren();
            WebspiderScannerNosqlImport google = new WebspiderScannerNosqlImport(sUrl, false);
            google.crawlUrls(true);
            //this.list = MyDataLoader.getInstance().getDomainsFromUrl(sUrl);
            list = Iw3dInternetNode.query( "client-"+DataConfiguration.SESSION_ID.toString() );
            if (!list.isEmpty()) {
                this.addMatrix(assetManager, nUniverse3d, list, etype);
            } else
                System.err.println("No data loaded");
        }

        return list;

    }

}
