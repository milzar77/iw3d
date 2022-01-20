package com.blogspot.fravalle.iw3d.jme.simpleapplication;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.iw3d.jme.sources.ESourceSelector;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


public class BrowserScreenController extends AbstractAppState implements ScreenController {

    private WindowJme3DSimpleApplication application;

    public BrowserScreenController(WindowJme3DSimpleApplication app) {
        this.application = app;
        this.nifty = app.nifty;
    }

    private Nifty nifty;
    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind(" + screen.getScreenId() + ")");
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit() {
        application.stop();
    }

    public void sample() {
        System.out.println("SAMPLE!");
    }


    public void surf(String inputId, String inputSpideringDepthLevel) {
        //this.application.applyRandomWebMatrix();
        String s1 = ((TextField)this.screen.findNiftyControl(inputId, TextField.class)).getRealText();
        String s2 = ((TextField)this.screen.findNiftyControl(inputSpideringDepthLevel, TextField.class)).getRealText();
        //TODO: download source from http and parse for links
        System.out.printf("Surfing around domain [%1$s] with spidering level %2$s\n",s1, s2);
        DataConfiguration.SIMPLE_STRING = s1;
        DataConfiguration.SPIDER_DEPTH_LEVEL = s2;
        this.application.selectedSource = ESourceSelector.SPIDER;
        this.application.initSurf(s1,s2);
    }

    public void showScreen(String screenId) {
        this.nifty.gotoScreen(screenId);
    }

    public void surfHome(String inputId, String inputSpideringDepthLevel) {
        ((TextField)this.screen.findNiftyControl(inputId, TextField.class)).setText("www.google.com");
        ((TextField)this.screen.findNiftyControl(inputSpideringDepthLevel, TextField.class)).setText("2");
        this.application.selectedSource = ESourceSelector.SPIDER;
        this.application.applyWebMatrix();
    }

    public void surfBookmarks(String inputId, String inputSpideringDepthLevel) {
        ((TextField)this.screen.findNiftyControl(inputId, TextField.class)).setText("");
        ((TextField)this.screen.findNiftyControl(inputSpideringDepthLevel, TextField.class)).setText("");
        this.application.selectedSource = ESourceSelector.BOOKMARKS;
        this.application.applyWebMatrix();
    }

    public void proposeUrl(String url) {
        ((TextField)this.screen.findNiftyControl("domain", TextField.class)).setText(url);
    }

    public void showOptionsScreen() {

        application.getStateManager().detach(application.niftyScreenBrowser);

        application.getStateManager().attach(application.niftyScreenOptions);

        //this.nifty.gotoScreen("options");

    }

    //APP STATE

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        if(!this.nifty.getAllScreensName().contains("browser")) {
            nifty.fromXml("res/gui/BrowserMenu.xml", "browser", this);
        } else {
            nifty.gotoScreen("browser");
        }

    }


}