package com.blogspot.fravalle.data.google;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.*;
import com.blogspot.fravalle.data.chrome.ChromeBookmarkImporter;
import com.blogspot.fravalle.data.utils.LibSqlUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class GoogleSearchImporter {

    private static final String FILE_SOURCE_INPUT = "/tmp/url.html";

    public enum IWEBIPV4 {
        iwid4, iwdomainname, iwurl, iwtitle, iwcategoryname, iwcategoryid, iwwebshotid, iwhashttps,
    }

    public enum ITEMPROUTESIPV4 {
        itrdomainname, itripaddress, itrhop, itiwid4,
    }

    private File fOut;
    private Vector<UrlDomain> domains = new Vector<UrlDomain>();
    private StringBuffer sb;

    public GoogleSearchImporter() {
        fOut = new File(FILE_SOURCE_INPUT);
        this.downloadUrl();
        try {
            this.beautify();
            this.parseSourceDownload();
            this.createSqlDmlImport();
            this.runImport(DataConfiguration.getDmlData());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("MY CRAWLED DOMAINS: "+domains);
        }
    }

    private void createSqlDmlImport() {
        //DataConfiguration.SIMPLE_STRING
        int limitCounter = 0;

        String dmlImport1 = LibSqlUtils.buildSqlInsertFromEnum( IWEBIPV4.class );

        FileOutputStream fos = null;
        try {
            if (!DataConfiguration.isDerbyCached()) {
                fos = new FileOutputStream(DataConfiguration.getDmlData(), false);

                for (UrlDomain dom : domains) {

                            System.out.println("DOMAIN: " + dom.getIwDomainName() + "; PATH: " + dom.getIwUrl());

                            /*if ((limitCounter++)>DataConfiguration.getMaxImportRows()) {
                                break;
                            }*/

                            String[] args = new String[IWEBIPV4.values().length];
                            args[0] = LibSqlUtils.formatForSqlInsert(dom.getId());
                            args[1] = LibSqlUtils.formatForSqlInsert(dom.getIwDomainName());
                            args[2] = LibSqlUtils.formatForSqlInsert(dom.getIwUrl());
                            System.out.println("TITLE="+dom.getIwTitle());
                            args[3] = LibSqlUtils.formatForSqlInsert(dom.getIwTitle());
                            args[4] = LibSqlUtils.formatForSqlInsert(dom.getIwCategoryName());
                            args[5] = LibSqlUtils.formatForSqlInsert(0);
                            args[6] = LibSqlUtils.formatForSqlInsert(DataConfiguration.SESSION_ID);
                            args[7] = "false";
                            fos.write(String.format(dmlImport1 + "\n", args).getBytes(Charset.defaultCharset()));

                }
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void beautify() throws IOException {
        File fOutBeautify = new File(FILE_SOURCE_INPUT+".beautify");
        Reader in1 = new FileReader(FILE_SOURCE_INPUT);
        BufferedReader br1 = new BufferedReader(in1);

        String lineImport = null;
        sb = new StringBuffer(1024);

        while ((lineImport=br1.readLine()) != null) {

            if (lineImport != null && lineImport.contains(">")) {
                String[] tokens = lineImport.split(">");
                for (String s : tokens) {
                    sb.append(s + ">\n");
                }
            } else {
                sb.append("\t"+lineImport+"\n");
            }
        }
        //System.out.println("HTML CONTENT:\n"+sb.toString());
    }


    private void parseSourceDownload() throws IOException {
        String[] splitSource = sb.toString().split("\n");
        for (String s : splitSource) {
            //System.out.println("SOURCE="+s);
            if (s.contains("href=\"")) {
                UrlDomain urlDomain = UrlDomain.parseUrlDomainUrl("Default", s);
                domains.add(urlDomain);
                //System.out.println("MYURL="+urlDomain.getIwDomainName()+"||"+urlDomain.getIwUrl());
            }
        }
    }

    private void downloadUrl() {
        try {
            URL urlDownload = new URL("https://"+ DataConfiguration.SIMPLE_STRING);
            FileOutputStream fos = new FileOutputStream(fOut);
            IOUtils.copy(urlDownload, fos);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runImport(String importFile) {
        System.err.println("TEST RUN IMPORT FROM " + importFile);
        try {
            String urlConnection = "jdbc:derby:"+ DataConfiguration.getBaseDerbyDirectory() + DataConfiguration.getDdlSchemaName() +";create=true";
            con = DriverManager.getConnection(urlConnection);
            statement = con.createStatement();
            this.populateWithDmlData(statement, importFile);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            this.closeChannels();
        }
        //DataLoader
    }

    private String lineInput = null;
    private Connection con;
    private Statement statement;

    public void closeChannels() {
        System.err.println("CLOSING DATA CHANNELS");
        try {
            if (statement!=null) statement.close();
            if (con!=null) con.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void populateWithDmlData(Statement statement, String inputData) throws IOException, SQLException {
        BufferedReader brInput = new BufferedReader(new FileReader(inputData));

        int importCounter = 0;
        while ((lineInput=brInput.readLine())!=null) {
            lineInput = lineInput.replace("\r\n","").replace(";","");
            if (!lineInput.startsWith("--")) {
                try {
                    statement.execute(lineInput);
                } catch (Exception e) {
                    System.err.println("--ERROR DEBUG INFO:\n----\n" + lineInput+"\n----");
                    throw e;
                } finally {
                    importCounter++;
                }
            }

        }
        brInput.close();
    }

}


