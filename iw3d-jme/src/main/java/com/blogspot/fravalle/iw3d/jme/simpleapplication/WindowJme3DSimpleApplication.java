package com.blogspot.fravalle.iw3d.jme.simpleapplication;

import com.blogspot.fravalle.aws.dynamodb.beans.Iw3dInternetNode;
import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.ColumnToSearch;
import com.blogspot.fravalle.data.IMyActionExecutor;
import com.blogspot.fravalle.iw3d.jme.sources.*;
import com.blogspot.fravalle.iw3d.jme.viewers.EViewerType;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireFrustum;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import jme3test.post.BloomUI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WindowJme3DSimpleApplication extends SimpleApplication implements IMyActionExecutor {

    //static protected DebugSwingJmeUserInterface gui;

    /*static {
        DataLoader.getInstance();
    }*/


    public ESourceSelector selectedSource = ESourceSelector.SPIDER;

    private IRenderingSource irs = null;

    private List<Iw3dInternetNode> list = new ArrayList<>();

    private Node nUniverse3d = new Node("Universe");

    public Nifty nifty;

    //private BrowserScreenController browserScreen;

    public BrowserScreenController niftyScreenBrowser;

    public OptionsScreenController niftyScreenOptions;

    private WireFrustum frustum;
    private FilterPostProcessor fpp;


    public static void main(String[] args) {

        WindowJme3DSimpleApplication app = new WindowJme3DSimpleApplication();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("iWorld 3D");
        app.setSettings(settings);

        app.start();

    }

        @Override
        public void simpleInitApp() {

            //gui = new DebugSwingJmeUserInterface(this);

            this.initKeys();

            //TODO: change input viewer
            //com.jme3.input.

            flyCam.setEnabled(true);
            flyCam.setDragToRotate(true);
            flyCam.setZoomSpeed(25f);

            cam.setLocation(new Vector3f(-10.6f, -0.25f, 1.15f));
            cam.setRotation(new Quaternion(-0.025f, 0.78f, 0.03f, 0.61f));

            rootNode.attachChild(nUniverse3d);

            //JmeDomainLibrary.getInstance().addHorizontalMatrix(assetManager, nUniverse3d);
            //JmeDomainLibrary.getInstance().addBookmarkImportWebMatrix(assetManager, nUniverse3d);
            //JmeDomainLibrary.getInstance().addCircularMatrix(assetManager, nUniverse3d);

            NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                    assetManager,
                    inputManager,
                    audioRenderer,
                    guiViewPort);
            nifty = niftyDisplay.getNifty();
            //nifty.enableAutoScaling(800,600);

            /*nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");*/
/*
            //StartScreenController startScreen = new StartScreenController(this);
            browserScreen = new BrowserScreenController(this);
            //nifty.fromXml("res/gui/OptionsMenu.xml", "start", startScreen);
            nifty.fromXml("res/gui/OptionsMenu.xml", "browser", browserScreen);
*/
/*
            // <screen>
            nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
                controller(new DefaultScreenController()); // Screen properties

                // <layer>
                layer(new LayerBuilder("Layer_ID") {{
                    childLayoutVertical(); // layer properties, add more...

                    // <panel>
                    panel(new PanelBuilder("Panel_ID") {{
                        childLayoutCenter(); // panel properties, add more...

                        // GUI elements
                        control(new ButtonBuilder("Button_ID", "Hello Nifty"){{
                            alignCenter();
                            valignCenter();
                            height("5%");
                            width("15%");
                        }});

                        //.. add more GUI elements here

                    }});
                    // </panel>
                }});
                // </layer>
            }}.build(nifty));
            // </screen>

            nifty.gotoScreen("Screen_ID"); // start the screen
*/
            // attach the nifty display to the gui view port as a processor
            guiViewPort.addProcessor(niftyDisplay);

            //TODO: nifty.enableAutoScaling(width, height);

            niftyScreenBrowser = new BrowserScreenController(this);
            niftyScreenOptions = new OptionsScreenController(this);

            stateManager.attach(niftyScreenBrowser);
/* START-OF: BLOOM */

            //viewPort.setBackgroundColor(ColorRGBA.fromRGBA255(74, 134, 232, 0));
            viewPort.setBackgroundColor(ColorRGBA.fromRGBA255(48, 70, 111, 0));

            fpp=new FilterPostProcessor(assetManager);
            fpp.setNumSamples(4);

            /*int numSamples = getContext().getSettings().getSamples();
            System.out.println("NUM SAMPLES: " + numSamples);
            if( numSamples > 0 ) {
                fpp.setNumSamples(numSamples);
            }*/

            BloomFilter bloom=new BloomFilter();
            bloom.setDownSamplingFactor(2);
            bloom.setBlurScale(1.16f);
            bloom.setExposurePower(0.94f);
            bloom.setExposureCutOff(0.145f);
            bloom.setBloomIntensity(-0.809f);

            /*
            bloom.setBlurScale(1.37f);
            bloom.setExposurePower(3.30f);
            bloom.setExposureCutOff(0.2f);
            bloom.setBloomIntensity(2.45f);


blurScale : 1.1400003
blurScale : 1.1500003
blurScale : 1.1600003
exposurePower : 0.9200022
exposurePower : 0.9300022
exposurePower : 0.9400022
exposurePower : 0.9500022
exposure CutOff : 0.14500014
exposure CutOff : 0.14600015
bloom Intensity : -0.9299973
bloom Intensity : -0.91999733
             */

            BloomUI ui=new BloomUI(inputManager, bloom);

            viewPort.addProcessor(fpp);
            fpp.addFilter(bloom);

            DirectionalLight light=new DirectionalLight();
            light.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
            light.setColor(ColorRGBA.White.mult(1.5f));
            //rootNode.addLight(light);

/* END-OF: BLOOM */

        }

        @Override
        public void simpleUpdate(float tpf) {
            //TODO: add update code
            super.simpleUpdate(tpf);
        }

    private void initKeys() {
        inputManager.addMapping("StarSelection",
                new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, "StarSelection");
        inputManager.setCursorVisible(true);
        //inputManager.addListener(analogListener, "StarSelection");
    }


    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("StarSelection")) {
                System.err.println("NAME: "+name);
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                // (Print the results so we see what is going on:)
                for (int i = 0; i < results.size(); i++) {
                    // (For each "hit", we know distance, impact point, geometry.)
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().getName();
                    //System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
                }
                // Use the results -- we rotate the selected geometry.
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Geometry target = results.getClosestCollision().getGeometry();
                    // Here comes the action:
                    /*if (target.getName().equals("Red Box")) {
                        target.rotate(0, -intensity, 0);
                    } else if (target.getName().equals("Blue Box")) {
                        target.rotate(0, intensity, 0);
                    }*/
                    System.err.println("TARGET NAME: "+target.getName());
                    System.err.println("TARGET DOMAIN HOST: "+target.getUserData("domainHostName"));
                    System.err.println("TARGET DOMAIN PATH: "+target.getUserData("domainHostPath"));
                    System.err.println("TARGET CATEGORY: "+target.getUserData("domainCategory"));

                    //gui.fireMyAction( target.getName(), target.getUserData("starId"));
                    WindowJme3DSimpleApplication.this.enqueue(new Callable<Void>(){
                        @Override
                        public Void call(){
                            if (WindowJme3DSimpleApplication.this instanceof SimpleApplication){
                                SimpleApplication simpleApp = (SimpleApplication) WindowJme3DSimpleApplication.this;
                                //TODO: simpleApp.getFlyByCamera().setDragToRotate(false);
                                //gui.fireMyAction( target.getName(), target.getUserData("starId"));
                                String url = target.getUserData("domainHostName").toString() + target.getUserData("domainHostPath").toString();
                                System.out.printf("URL PROPOSED: %1$s", url);
                                WindowJme3DSimpleApplication.this.proposeUrl(url);
                            }
                            return null;
                        }
                    });
                }
            } // else if ...
            /*if (name.equals("StarSelection") && !keyPressed) {
                // 1. Reset results list.
                CollisionResults results = new CollisionResults();
                // 2. Aim the ray from cam loc to cam direction.
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                // 3. Collect intersections between Ray and Shootables in results list.
                // DO NOT check collision with the root node, or else ALL collisions will hit the
                // skybox! Always make a separate node for objects you want to collide with.
                nUniverse3d.collideWith(ray, results);
                // 4. Print the results
                System.out.println("----- Collisions? " + results.size() + "-----");
                for (int i = 0; i < results.size(); i++) {
                    // For each hit, we know distance, impact point, name of geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String hit = results.getCollision(i).getGeometry().getName();
                    System.out.println("* Collision #" + i);
                    System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                    gui.fireMyAction( results.getCollision(i).getGeometry().getName(), results.getCollision(i).getGeometry().getUserData("starId"));
                }
                // 5. Use the results (we mark the hit object)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    // Let's interact - we mark the hit with a red dot.
                    //mark.setLocalTranslation(closest.getContactPoint());
                    //rootNode.attachChild(mark);
                } else {
                    // No hits? Then remove the red mark.
                    //rootNode.detachChild(mark);
                }
            }*/
        }
    };

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("StarSelection")) {
                System.err.println("NAME: "+name);
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                // (Print the results so we see what is going on:)
                for (int i = 0; i < results.size(); i++) {
                    // (For each "hit", we know distance, impact point, geometry.)
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().getName();
                    System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
                }
                // Use the results -- we rotate the selected geometry.
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Geometry target = results.getClosestCollision().getGeometry();
                    // Here comes the action:
                    if (target.getName().equals("Red Box")) {
                        target.rotate(0, -intensity, 0);
                    } else if (target.getName().equals("Blue Box")) {
                        target.rotate(0, intensity, 0);
                    }
                    System.err.println("TARGET NAME: "+target.getName());
                    System.err.println("TARGET VALUE: "+target.getUserData("domainHostName"));
                }
            } // else if ...
        }
    };

    @Override
    public void loadFilter(ColumnToSearch[] columns) {
        //Property<BigDecimal> columnName, BigDecimal starValue
        System.err.println("DEBUG TEST FIRE EVENT!!!");
        if (rootNode.getChildren().size()>0) {
            System.err.println("DEBUG LOADING FILTERED STARS!!!");
            //TODO: JmeStarLibrary.getInstance().addFilteredStars(assetManager, nUniverse3d, columns);
        }

    }

    public void applyCircularMatrix(boolean useSurfing) {

        System.out.println("Passato di qui per Circular");

        switch (selectedSource) {
            case SPIDER:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.CIRCULAR);
                break;
            case BOOKMARKS:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.CIRCULAR);
                break;
        }
        //if (!useSurfing) {
        /*} else {
            System.err.println("Not Yet Implemented! USING OLD WAY");
            JmeDomainLibrary.getInstance().applySurfingCircularMatrix(assetManager, nUniverse3d);
        }*/

/*
        WindowJme3DSimpleApplication.creaSessione();

        nUniverse3d.detachAllChildren();
        if (!useSurfing) {
            DataConfiguration.SESSION_ID = DataConfiguration.STARTING_SESSION_ID;
            JmeDomainLibrary.getInstance().addBookmarkImportCircularMatrix(assetManager, nUniverse3d);
        } else {
            //JmeDomainLibrary.getInstance().addSurfingCircularMatrix(DataConfiguration.SIMPLE_STRING, assetManager, nUniverse3d);
            JmeDomainLibrary.getInstance().applySurfingCircularMatrix(assetManager, nUniverse3d);
        }
 */
    }

    public void applyQuadMatrix(boolean useSurfing) {

        System.out.println("Passato di qui per QUAD");

        switch (selectedSource) {
            case SPIDER:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.QUAD);
                break;
            case BOOKMARKS:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.QUAD);
                break;
        }
        /*
        WindowJme3DSimpleApplication.creaSessione();

        nUniverse3d.detachAllChildren();
        if (!useSurfing) {
            DataConfiguration.SESSION_ID = DataConfiguration.STARTING_SESSION_ID;
            JmeDomainLibrary.getInstance().addBookmarkImportHorizontalMatrix(assetManager, nUniverse3d);
        } else {
            //JmeDomainLibrary.getInstance().addSurfingHorizontalMatrix(DataConfiguration.SIMPLE_STRING, assetManager, nUniverse3d);
            JmeDomainLibrary.getInstance().applySurfingHorizontalMatrix(assetManager, nUniverse3d);
        }
        */
    }

    public void applyWebMatrix() {

        System.out.println("Passato di qui per WEB MATRIX");

        switch (selectedSource) {
            case SPIDER:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.WEB);
                break;
            case BOOKMARKS:
                irs.addMatrix(assetManager, nUniverse3d, list, EViewerType.WEB);
                break;
        }
    }

    public void initRandomWebMatrix(boolean useSurfing) {

        System.out.println("Passato di qui per RANDOM WEB MATRIX");

        switch (selectedSource) {
            case SPIDER:
                irs = new JmeSpiderSource();
                this.list = irs.applyMatrix(DataConfiguration.SIMPLE_STRING, assetManager, nUniverse3d, EViewerType.WEB);
                break;
            case BOOKMARKS:
                irs = new JmeBookmarkSource();
                this.list = irs.applyMatrix(null, assetManager, nUniverse3d, EViewerType.WEB);
                break;
        }
        /*
        WindowJme3DSimpleApplication.creaSessione();

        nUniverse3d.detachAllChildren();
        if (!useSurfing) {
            DataConfiguration.SESSION_ID = DataConfiguration.STARTING_SESSION_ID;
            JmeDomainLibrary.getInstance().addBookmarkImportWebMatrix(assetManager, nUniverse3d);
        } else {
            JmeDomainLibrary.getInstance().applySurfingRandomWebMatrix(assetManager, nUniverse3d);
        }
        */
    }

    public void applyMaximumItems(Integer i) {

        System.out.println("BOOKMARK Passato di qui per WEB MATRIX");
        IRenderingSource irs = null;
        //if (!useSurfing) {
        irs = new JmeBookmarkSource();
        this.list = irs.applyMatrix(null, assetManager, nUniverse3d, EViewerType.WEB);
    }

    public void initSurf(String sUrl, String spiderDepthLevel) {

        System.out.println("SURF Passato di qui per WEB MATRIX");
        IRenderingSource irs = null;
        //if (!useSurfing) {
        irs = new JmeSpiderSource();
        this.list = irs.applyMatrix(sUrl, assetManager, nUniverse3d, EViewerType.WEB);

    }

    /*@Override
    public void starNeighboursSelection(List<Hygimport> starNeighbours) {
        //TODO: JmeStarLibrary.getInstance().starSelection(assetManager, nUniverse3d, starNeighbours);
    }*/

    private void proposeUrl(String url) {

        this.niftyScreenBrowser.proposeUrl(url);

    }

    public static void creaSessione() {
        DataConfiguration.staccaSessione();
    }


}
