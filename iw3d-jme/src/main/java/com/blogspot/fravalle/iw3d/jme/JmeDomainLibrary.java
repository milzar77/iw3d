package com.blogspot.fravalle.iw3d.jme;

public class JmeDomainLibrary {

    private static JmeDomainLibrary instance;

    private JmeDomainLibrary() {
    }

    public static JmeDomainLibrary getInstance() {
        if (instance==null) {
            instance = new JmeDomainLibrary();
        }
        return instance;
    }

}
