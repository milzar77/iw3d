package com.blogspot.fravalle.iw3d.jme.simpleapplication;

import com.blogspot.fravalle.core.DataConfiguration;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


public class OptionsScreenController extends AbstractAppState implements ScreenController {

    final private WindowJme3DSimpleApplication application;

    public OptionsScreenController(WindowJme3DSimpleApplication app) {
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

    public void surfCircularMatrix() {
        this.application.applyCircularMatrix(true);
    }

    public void surfQuadMatrix() {
        this.application.applyQuadMatrix(true);
    }

    public void surfRandomWebMatrix() {
        this.application.applyRandomWebMatrix(true);
    }

    //APP STATE

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        if(!this.nifty.getAllScreensName().contains("options")) {
            nifty.fromXml("res/gui/OptionsMenu.xml", "options", this);
        } else {
            nifty.gotoScreen("options");
        }

    }

    public void backBrowserLocation() {

        application.getStateManager().detach(application.niftyScreenOptions);

        application.getStateManager().attach(application.niftyScreenBrowser);

        //this.nifty.gotoScreen("browser");

    }

}