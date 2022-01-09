package com.blogspot.fravalle.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class XMLResources {
    private static String XML = "xml";

    private static ResourceBundle.Control resControl = new ResourceBundle.Control() {
        public List<String> getFormats(String baseName) {
            if (baseName == null)
                throw new NullPointerException();
            return Arrays.asList(XML);
        }
        public ResourceBundle newBundle(String baseName,
                                        Locale locale,
                                        String format,
                                        ClassLoader loader,
                                        boolean reload)
                throws IllegalAccessException,
                InstantiationException,
                IOException {
            if (baseName == null || locale == null
                    || format == null || loader == null)
                throw new NullPointerException();
            ResourceBundle bundle = null;
            if (format.equals(XML)) {
                String bundleName = toBundleName(baseName, locale);
                String resourceName = toResourceName(bundleName, format);
                InputStream stream = null;
                if (reload) {
                    URL url = loader.getResource(resourceName);
                    if (url != null) {
                        URLConnection connection = url.openConnection();
                        if (connection != null) {
                            // Disable caches to get fresh data for
                            // reloading.
                            connection.setUseCaches(false);
                            stream = connection.getInputStream();
                        }
                    }
                } else {
                    stream = loader.getResourceAsStream(resourceName);
                }
                if (stream != null) {
                    BufferedInputStream bis = new BufferedInputStream(stream);
                    bundle = new XMLResourceBundle(bis);
                    bis.close();
                }
            }
            return bundle;
        }
    };

    private static XMLResources instance;
    private static HashMap<String, ResourceBundle> resourceBundles;

    private XMLResources() {
        this.resourceBundles = new HashMap<String, ResourceBundle>();
    }

    public static ResourceBundle getBundle(String keySet) {
        if (instance == null) {
            instance = new XMLResources();
        }
        if (!resourceBundles.containsKey(keySet)) {
            ResourceBundle rb = ResourceBundle.getBundle(keySet, resControl);
            resourceBundles.put(keySet, rb);
        }
        return resourceBundles.get(keySet);
    }

    public static void main(String args[]) {
        /*String string = XMLResources.getBundle("data-sources").getString("url.astronexus");
        System.out.println("Key value: " + string);*/
    }
}

class XMLResourceBundle extends ResourceBundle {
    private Properties props;

    XMLResourceBundle(InputStream stream) throws IOException {
        props = new Properties();
        props.loadFromXML(stream);
    }

    protected Object handleGetObject(String key) {
        return props.getProperty(key);
    }

    public Enumeration<String> getKeys() {
        Set<String> handleKeys = props.stringPropertyNames();
        return Collections.enumeration(handleKeys);
    }
}