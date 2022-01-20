package com.blogspot.fravalle.iw3d.jme.sources;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.util.BufferUtils;

import java.util.*;

abstract public class ARenderingViewer implements IRenderingViewer {

    protected Random rand = new Random(1000);

    protected HashMap<String, Vector<Iw3dInternetNode>> hm = new LinkedHashMap<String, Vector<Iw3dInternetNode>>();

    private Vector3f previousNodeVector = new Vector3f(0F, 0F, 0F);

    protected void fillWithUserData(Geometry geomDomain, Iw3dInternetNode node) {
        geomDomain.setUserData("domainId", node.getIwid());
        geomDomain.setUserData("domainHostName", node.getIwdomainname());
        geomDomain.setUserData("domainHostPath", node.getIwurl());
        //FIX: geomDomain.setUserData("domainCategory", node.getIwcategoryname());
    }

    @Override
    public void traverseSceneForConnection(AssetManager assetManager, Node nUniverse3d) {

        for (String key : hm.keySet()) {
            List<Node> l1 = nUniverse3d.descendantMatches(Node.class, "Category_" + key);
            Vector<Iw3dInternetNode> v = hm.get(key);
            //System.out.println("TRAVERSAL RESULT SIZE:" + v.size() + "; " + l1 );
            for (Iw3dInternetNode n : v) {
                Vector3f vStart = Vector3f.ZERO;
                Vector3f vEnd = Vector3f.ZERO;

                List<Spatial> l2 = nUniverse3d.descendantMatches(Spatial.class, "domainId_" + n.getIwid());
                int idx = v.indexOf(n);
                Iw3dInternetNode nPrevious = (idx==-1||idx==0) ? null : v.get(idx-1);
                if (nPrevious!=null) {
                    List<Spatial> l3 = nUniverse3d.descendantMatches(Spatial.class, "domainId_" + nPrevious.getIwid());
                    vStart = l3.get(0).getWorldTranslation();
                    vEnd = l2.get(0).getWorldTranslation();
                    //System.out.printf("FOUND PREVIOUS DOMAIN: %s [%s] FOR CURRENT DOMAIN %s [%s]", nPrevious, vStart, n, vEnd );
                } else {
                    vStart = l1.size()>0 ? l1.get(0).getWorldTranslation() : Vector3f.ZERO;
                    vEnd = l2.get(0).getWorldTranslation();
                }

                l1.get(0).attachChild( this.connectWithPreviousNode(assetManager, vStart, vEnd) );


            }
        }

    }

    private Geometry connectWithOriginNode(AssetManager assetManager, Vector3f vEnd) {
        Vector3f vStart = new Vector3f(0F, 0F, 0F);
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);

        Vector3f[] lineVerticies=new Vector3f[2];
        lineVerticies[0]=vStart;
        lineVerticies[1]=vEnd;
        /*
        lineVerticies[0]=new Vector3f(2,0,0);
        lineVerticies[1]=new Vector3f(-1,0,1);
        lineVerticies[2]=new Vector3f(0,1,1);
        lineVerticies[3]=new Vector3f(1,1,1);
        lineVerticies[4]=new Vector3f(1,4,0);
        */

        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

        short[] indexes=new short[2*lineVerticies.length]; //Indexes are in pairs, from a vertex and to a vertex

        for(short i=0;i<lineVerticies.length-1;i++){
            indexes[2*i]=i;
            indexes[2*i+1]=(short)(i+1);
        }

        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, indexes);

        lineMesh.updateBound();
        lineMesh.updateCounts();

        /*lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ 0, 0, 0, 0, 1, 0});
        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });*/
//lineMesh.updateBound();
//lineMesh.updateCounts();
        Geometry lineGeometry = new Geometry("line", lineMesh);
        Material lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", ColorRGBA.Blue);
        lineGeometry.setMaterial(lineMaterial);
        return lineGeometry;

    }

    private Geometry connectWithPreviousNode(AssetManager assetManager, Vector3f vStart, Vector3f vEnd) {
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);

        Vector3f[] lineVerticies=new Vector3f[2];
        lineVerticies[0]=vStart;
        lineVerticies[1]=vEnd;
        /*
        lineVerticies[0]=new Vector3f(2,0,0);
        lineVerticies[1]=new Vector3f(-1,0,1);
        lineVerticies[2]=new Vector3f(0,1,1);
        lineVerticies[3]=new Vector3f(1,1,1);
        lineVerticies[4]=new Vector3f(1,4,0);
        */

        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

        short[] indexes=new short[2*lineVerticies.length]; //Indexes are in pairs, from a vertex and to a vertex

        for(short i=0;i<lineVerticies.length-1;i++){
            indexes[2*i]=i;
            indexes[2*i+1]=(short)(i+1);
        }

        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, indexes);

        lineMesh.updateBound();
        lineMesh.updateCounts();

        /*lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ 0, 0, 0, 0, 1, 0});
        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });*/
//lineMesh.updateBound();
//lineMesh.updateCounts();
        Geometry lineGeometry = new Geometry("line", lineMesh);
        Material lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", ColorRGBA.Blue);
        lineGeometry.setMaterial(lineMaterial);
        return lineGeometry;

    }

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


}
