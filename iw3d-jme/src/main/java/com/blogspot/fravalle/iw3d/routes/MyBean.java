package com.blogspot.fravalle.iw3d.routes;

import com.blogspot.fravalle.iw3d.jme.BrowserIWorld3D;
import com.blogspot.fravalle.iw3d.jme.SwingBookmarkImporter;

public class MyBean {

    public void doSomething() {
        System.out.println("Hello World!");

        SwingBookmarkImporter.ImportTask imp = (SwingBookmarkImporter.ImportTask) BrowserIWorld3D.camelContext.getRegistry().lookupByName("bookmarkImporter");
        imp.execute();
        //bind("bookmarkImporter", SwingBookmarkImporter.ImportTask.class, importTask);

    }

}
