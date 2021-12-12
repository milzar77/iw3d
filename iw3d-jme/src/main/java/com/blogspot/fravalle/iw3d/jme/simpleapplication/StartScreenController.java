package com.blogspot.fravalle.iw3d.jme.simpleapplication;

import com.blogspot.fravalle.core.DataConfiguration;
import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.examples.textfield.TextFieldDemoStartScreen;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


public class StartScreenController implements ScreenController {

    final private WindowJme3DSimpleApplication application;

    public StartScreenController(WindowJme3DSimpleApplication app) {
        this.application = app;
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

    public void sampleCircularMatrix() {
        this.application.applyCircularMatrix(false);
    }

    public void sampleQuadMatrix() {
        this.application.applyQuadMatrix(false);
    }

    public void sampleWebMatrix() {
        this.application.applyWebMatrix();
    }

    public void sampleRandomWebMatrix() {
        this.application.applyRandomWebMatrix(false);
    }

    public void surf(String inputId) {
        //this.application.applyRandomWebMatrix();
        String s = ((TextField)this.screen.findNiftyControl(inputId, TextField.class)).getRealText() + "/";
        //TODO: download source from http and parse for links
        System.out.printf("Surfing around domain [%1$s]\n",s);
        DataConfiguration.SIMPLE_STRING = s;
        this.application.surf(s);
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

}