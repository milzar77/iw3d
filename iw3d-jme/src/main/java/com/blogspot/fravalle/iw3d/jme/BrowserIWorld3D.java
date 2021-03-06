package com.blogspot.fravalle.iw3d.jme;

import com.blogspot.fravalle.data.IMyActionExecutor;
import com.blogspot.fravalle.iw3d.jme.simpleapplication.WindowJme3DSimpleApplication;
import com.blogspot.fravalle.iw3d.routes.BookmarkRouteBuilder;
import com.jme3.app.LegacyApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;

public class BrowserIWorld3D {

    public static SpringCamelContext camelContext;

    public static void main(String[] args) {

        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                    "file:src/main/resources/META-INF/spring/iw3d-spring-context.xml");

            camelContext = (SpringCamelContext) applicationContext.getBean("camel");
            //camelContext.addRoutes(new BookmarkRouteBuilder());
            camelContext.start();
            /* pure camel way
            camelContext = new DefaultCamelContext();
            camelContext.addRoutes(new BookmarkRouteBuilder());
            camelContext.start();
             */
        } catch (Exception e) {
            e.printStackTrace();
        }


        createCanvas(appClass);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){


                SwingDatabaseSetup dbSetup = new SwingDatabaseSetup();
                dbSetup.doDbSetup();

                while (!SwingDatabaseSetup.isSetupFinished) {
                    try {
                        //System.err.println("Waiting progress...");
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }

                JPopupMenu.setDefaultLightWeightPopupEnabled(false);

                createFrame();



                SwingBookmarkImporter instanceProgress = new SwingBookmarkImporter((WindowJme3DSimpleApplication) app);

                currentPanel.add(canvas3d, BorderLayout.CENTER);
                currentPanel.add(instanceProgress, BorderLayout.SOUTH);


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

    public BrowserIWorld3D(IMyActionExecutor parJmeInstance) {

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

    private static void createInterface(){

        canvasPanel1 = new JPanel();
        canvasPanel1.setLayout(new BorderLayout());

        canvasPanel2 = new JPanel();
        canvasPanel2.setLayout(new BorderLayout());
        //tabbedPane.addTab("jME3 Canvas 2", canvasPanel2);

        jFrame.getContentPane().add(canvasPanel1);

        currentPanel = canvasPanel1;
    }

    private static void createFrame(){
        jFrame = new JFrame("iWorld 3D Surfer");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //starmapWindow = new StarmapWindowForm();
        //jFrame.setContentPane(starmapWindow.windowPanel);

        jFrame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e) {
                camelContext.stop();
                app.stop();
                System.exit(0);
            }
        });

        createInterface();
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
        settings.setWidth(800);
        settings.setHeight(600);

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
