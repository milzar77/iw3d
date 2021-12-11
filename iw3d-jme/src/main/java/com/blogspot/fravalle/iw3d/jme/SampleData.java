package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.data.IMyActionExecutor;
import com.jme3.app.LegacyApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;

public class SampleData {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SwingBookmarkImporter.createAndShowGUI();
            }
        });

        while (!SwingBookmarkImporter.isSetupFinished) {
            try {
                //System.err.println("Waiting progress...");
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                return;
            }
        }

        createCanvas(appClass);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                JPopupMenu.setDefaultLightWeightPopupEnabled(false);

                createFrame();

                currentPanel.add(canvas3d, BorderLayout.CENTER);
                jFrame.pack();
                startApp();
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
            }
        });

    }

    private static LegacyApplication app;
    private static JFrame jFrame;
    private static Container currentPanel;

    //private static StarmapWindowForm starmapWindow;

    private static JmeCanvasContext context;
    public static Canvas canvas3d;
    private static Container canvasPanel1, canvasPanel2;

    private static final String appClass = "com.blogspot.fravalle.iw3d.jme.simpleapplication.WindowJme3DSimpleApplication";

    public SampleData(IMyActionExecutor parJmeInstance) {

        /*JFrame jf = new JFrame();
        starmapWindow = new StarmapWindowForm();
        jf.setContentPane(starmapWindow.windowPanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);*/

    }

    private static void createTabs(){
        //tabbedPane = new JTabbedPane();

        canvasPanel1 = new JPanel();
        canvasPanel1.setLayout(new BorderLayout());
        //tabbedPane.addTab("jME3 Canvas 1", canvasPanel1);

        canvasPanel2 = new JPanel();
        canvasPanel2.setLayout(new BorderLayout());
        //tabbedPane.addTab("jME3 Canvas 2", canvasPanel2);

        jFrame.getContentPane().add(canvasPanel1);

        currentPanel = canvasPanel1;
    }

    private static void createFrame(){
        jFrame = new JFrame("Star Map 3D X");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //starmapWindow = new StarmapWindowForm();
        //jFrame.setContentPane(starmapWindow.windowPanel);

        jFrame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e) {
                app.stop();
            }
        });

        createTabs();
        //createMenu();
    }

    public static void startApp(){
        app.startCanvas();
        app.enqueue(new Callable<Void>(){
            @Override
            public Void call(){
                if (app instanceof SimpleApplication){
                    SimpleApplication simpleApp = (SimpleApplication) app;
                    simpleApp.getFlyByCamera().setDragToRotate(true);
                }
                return null;
            }
        });

    }

    public static void createCanvas(String appClass){
        AppSettings settings = new AppSettings(true);
        settings.setWidth(640);
        settings.setHeight(480);

        try{
            Class clazz = Class.forName(appClass);
            app = (LegacyApplication)clazz.newInstance();
        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }catch (InstantiationException ex){
            ex.printStackTrace();
        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }

        app.setPauseOnLostFocus(false);
        app.setSettings(settings);
        app.createCanvas();
        app.startCanvas();

        context = (JmeCanvasContext) app.getContext();
        canvas3d = context.getCanvas();
        canvas3d.setSize(settings.getWidth(), settings.getHeight());
    }

}
