package com.blogspot.fravalle.core;

import java.io.File;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;

public class DataConfiguration {


    public static String SPIDER_DEPTH_LEVEL = "2";//TODO: prevedere stringa composta con eventuali info di spidering
    public static String SIMPLE_STRING = null;
    private static Random random = new Random(1000);
    public static Integer SESSION_ID = 1;
    public static final Integer STARTING_SESSION_ID = SESSION_ID;
    public static Integer staccaSessione(Integer i) {
        return SESSION_ID = i;
    }
    public static Integer staccaSessione() {
        return SESSION_ID = random.nextInt();
    }
    public static Integer getSpiderDepthLevel() {
        Integer s = Integer.parseInt(SPIDER_DEPTH_LEVEL);
        return s;
    }

    private static ResourceBundle rbConfig;

    public static final String TEMP_FILE_SUFFIX = ".temp";
    public static final Integer IMPORT_MAX_ROWS;

    private static final String KEY_USE_DERBY_CACHED = "use.derby.cached";
    private static final String KEY_TRANSFORMED_OUTPUT = "transformed.output";
    private static final String KEY_USE_DOWNLOAD_CACHED = "use.download.cached";
    private static final String KEY_DOWNLOAD_FILE = "download.file";
    private static final String KEY_IMPORT_URL = "import.url";
    private static final String KEY_IMPORT_DATA = "import.filename";
    private static final String KEY_IMPORT_ARCHIVE = "import.archive";
    private static final String KEY_DDL_SCHEMA_NAME = "db.schema.name";
    private static final String KEY_DDL_SCHEMA_CREATE = "db.ddl.create.schema";
    private static final String KEY_DML_DATA = "db.dml.import.data";
    private static final String KEY_DERBY_DB_DIR = "derby.base.directory";
    private static final String KEY_CAYENNE_PROJECT = "cayenne.project";
    private static final String MAX_ROWS = "import.maxrows";

    private Vector<File> directoriesToDelete;

    static {
        Integer i = null;
        try {
            rbConfig = ResourceBundle.getBundle("db-setup");

            String maxRows = rbConfig.getString(MAX_ROWS);
            i = Integer.parseInt(maxRows);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (i!=null)
                IMPORT_MAX_ROWS = i;
            else
                IMPORT_MAX_ROWS = Integer.valueOf(1000);
        }

        /*System.out.println("==> Attention! use db-setup.properties to set configuration <==");
        System.out.println(KEY_DDL_SCHEMA_NAME + " (db schema name): " + rbConfig.getString(KEY_DDL_SCHEMA_NAME));
        System.out.println(KEY_DDL_SCHEMA_CREATE + " (sql DDL script to create schema): " + rbConfig.getString(KEY_DDL_SCHEMA_CREATE));
        System.out.println(KEY_TRANSFORMED_OUTPUT + " (CSV data transformed): " + rbConfig.getString(KEY_TRANSFORMED_OUTPUT));
        System.out.println(KEY_DML_DATA + " (sql DML script to load transformed data): " + rbConfig.getString(KEY_DML_DATA));
        System.out.println(KEY_DERBY_DB_DIR + " (derby base db directory): " + rbConfig.getString(KEY_DERBY_DB_DIR));
        System.out.println(MAX_ROWS + " (maximum rows imported into derby): declared " + rbConfig.getString(MAX_ROWS) + " real " + IMPORT_MAX_ROWS);
        System.out.println(KEY_IMPORT_URL + " (import url): " + rbConfig.getString(KEY_IMPORT_URL));*/

    }

    public static String getBaseDerbyDirectory() {
        String derbyDirToUse = rbConfig.getString(KEY_DERBY_DB_DIR);
        if (derbyDirToUse==null) {
            derbyDirToUse = "/tmp/jme-db";
        }
        return derbyDirToUse;
    }

    public static Boolean isDerbyCached() {
        return Boolean.valueOf(rbConfig.getString(KEY_USE_DERBY_CACHED));
    }

    public static String getDdlSchemaName() {
        return rbConfig.getString(KEY_DDL_SCHEMA_NAME);
    }

    public static String getDdlSchemaCreation() {
        return rbConfig.getString(KEY_DDL_SCHEMA_CREATE);
    }

    public static String mustDownloadFile() {
        return rbConfig.getString(KEY_DOWNLOAD_FILE);
    }

    public static String useDownloadCached() {
        return rbConfig.getString(KEY_USE_DOWNLOAD_CACHED);
    }

    public static String useDerbyCached() {
        return rbConfig.getString(KEY_USE_DERBY_CACHED);
    }

    public static String getImportData() {
        return rbConfig.getString(KEY_IMPORT_DATA);
    }

    public static String getDmlData() { return rbConfig.getString(KEY_DML_DATA); }

    public static String getTransformedOutput() {
        return rbConfig.getString(KEY_TRANSFORMED_OUTPUT);
    }

    public static String getCayennProjectPath() {
        return rbConfig.getString(KEY_CAYENNE_PROJECT);
    }

    public static Integer getMaxImportRows() {
        return IMPORT_MAX_ROWS;
    }

}
