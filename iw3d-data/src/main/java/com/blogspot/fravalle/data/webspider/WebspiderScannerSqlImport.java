package com.blogspot.fravalle.data.webspider;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.*;
import com.blogspot.fravalle.data.utils.LibSqlUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class WebspiderScannerSqlImport {

    private static final String FILE_SOURCE_INPUT = "/tmp/url.html";

    public static HashMap<String, Integer> categories = new LinkedHashMap<String, Integer>();

    private String theUrl;
    private Boolean appendSqlOutput = Boolean.FALSE;


    public enum IWEBIPV4 {
        iwid4, iwdomainname, iwurl, iwtitle, iwcategoryname, iwcategoryid, iwwebshotid, iwwebdepthlevel, iwhttpcode, iwhashttps,
    }

    public enum ITEMPROUTESIPV4 {
        itrdomainname, itripaddress, itrhop, itiwid4,
    }

    private File fOut;
    private Vector<Vector<UrlDomain>> domains = new Vector<Vector<UrlDomain>>();
    private StringBuffer sb;
    private Integer currentDepthLevel;

    public WebspiderScannerSqlImport(String theUrl, Boolean appendSqlOutput) {
        if (this.theUrl==null) {
            currentDepthLevel = 0;
            if (domains.isEmpty()) {
                for (int i = 0; i < DataConfiguration.getSpiderDepthLevel(); i++) {
                    Vector<UrlDomain> mydoms = new Vector<UrlDomain>();
                    domains.add(i, mydoms);
                }
            }
         }
        this.theUrl = theUrl;
        this.appendSqlOutput = appendSqlOutput;
        fOut = new File(FILE_SOURCE_INPUT);
        this.doSomeWork();
    }

    private void doSomeWork() {
        this.downloadUrl();
        try {
            this.beautify();
            this.parseSourceDownload(currentDepthLevel);
            this.createSqlDmlImport(currentDepthLevel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("MY CRAWLED DOMAINS: "+domains);
        }
    }

    private void createSqlDmlImport(Integer depthLevel) {
        //DataConfiguration.SIMPLE_STRING
        int limitCounter = 0;

        String dmlImport1 = LibSqlUtils.buildSqlInsertFromEnum( IWEBIPV4.class );

        FileOutputStream fos = null;
        try {
            if (!DataConfiguration.isDerbyCached()) {
                fos = new FileOutputStream(DataConfiguration.getDmlData(), this.appendSqlOutput);
                System.out.printf("Dml Sql import data creator is %1$s...",  this.appendSqlOutput ? "APPENDING" : "CREATING FROM SCRATCH");
                if (domains.get(depthLevel)==null) {
                    domains.add(depthLevel, new Vector<UrlDomain>());
                    //domains.get(depthLevel).add(urlDomain);
                }
                for (UrlDomain dom : domains.get(depthLevel)) {

                            //System.out.println("DOMAIN: " + dom.getIwDomainName() + "; PATH: " + dom.getIwUrl());

                            /*if ((limitCounter++)>DataConfiguration.getMaxImportRows()) {
                                break;
                            }*/

                            String[] args = new String[IWEBIPV4.values().length];
                            args[0] = LibSqlUtils.formatForSqlInsert(dom.getId());
                            args[1] = LibSqlUtils.formatForSqlInsert(dom.getIwDomainName());
                            args[2] = LibSqlUtils.formatForSqlInsert(dom.getIwUrl());
                            args[3] = LibSqlUtils.formatForSqlInsert(dom.getIwTitle());
                            args[4] = LibSqlUtils.formatForSqlInsert(dom.getIwCategoryName());
                            args[5] = LibSqlUtils.formatForSqlInsert(dom.getIwCategoryId());
                            args[6] = LibSqlUtils.formatForSqlInsert(DataConfiguration.SESSION_ID);
                            args[7] = LibSqlUtils.formatForSqlInsert(dom.getDepthLevel());
                            args[8] = LibSqlUtils.formatForSqlInsert(dom.getIwhttpcode());
                            args[9] = "false";
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


    private void parseSourceDownload(Integer depthLevel) throws IOException {
        String[] splitSource = sb.toString().split("\n");
        String currentCategoryName = null;
        for (String s : splitSource) {
            //System.out.println("SOURCE="+s);
            if (s.contains("href=\"")) {

                UrlDomain urlDomain = UrlDomain.parseUrlDomainUrl("Default", s, depthLevel);
                if (domains.isEmpty()) {
                    domains.add(depthLevel, new Vector<UrlDomain>());
                    domains.get(depthLevel).add(urlDomain);
                } else {
                    domains.get(depthLevel).add(urlDomain);
                }
                if (currentCategoryName==null) {
                    currentCategoryName = "FV";
                    categories.put(currentCategoryName, categories.size()+1 );
                } else {
                    currentCategoryName = urlDomain.getIwDomainName();
                    categories.put(currentCategoryName, categories.size()+1 );
                }
                //System.out.println("MYURL="+urlDomain.getIwDomainName()+"||"+urlDomain.getIwUrl());
            }
        }

        for (UrlDomain dom : domains.get(depthLevel)){
            if (categories.get(dom.getIwDomainName())!=null) {
                dom.setIwCategoryId( categories.get(dom.getIwDomainName()) );
            } else {
                System.out.printf("ORPHAN CATEGORY NODE: %1$s CAT:%2$s\n", dom.getIwDomainName(), dom.getIwCategoryName());
            }

        }

    }

    private void downloadUrl() {
        try {
            URL urlDownload = new URL("https://"+ theUrl);
            FileOutputStream fos = new FileOutputStream(fOut);
            IOUtils.copy(urlDownload, fos);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            //e.printStackTrace(System.err);
            System.out.println("HTTP CODE 404 ON: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runImport(String importFile) {
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

    private void closeChannels() {
        System.err.println("CLOSING DATA CHANNELS");
        try {
            if (statement!=null) statement.close();
            if (con!=null) con.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void populateWithDmlData(Statement statement, String inputData) throws IOException, SQLException {
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

    public void crawlUrls(boolean bAppending) {
        this.appendSqlOutput = bAppending;
        System.out.printf("TOTAL CRAWLED DOMAINS: %1$s\n", domains.size());
        for (UrlDomain dom : domains.get(currentDepthLevel)) {
/*
            currentDepthLevel = 0;
            if (domains.get(currentDepthLevel)==null) {
                Vector<UrlDomain> mydoms = new Vector<>();
                domains.add(currentDepthLevel, mydoms);
            }
 */
            this.theUrl = dom.getIwDomainName() + dom.getIwUrl();
            //System.out.printf("CRAWLING URL WITH LEVEL [%1$s]: %2$s\n", currentDepthLevel, theUrl);
            //this.doSomeWork();

            this.downloadUrl();
            try {
                this.beautify();
                this.parseSourceDownload(currentDepthLevel+1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //System.out.println("OUTPUT CRAWLED DOMAINS: "+domains);
            }
        }

        this.createSqlDmlImport(currentDepthLevel+1);

        //TODO: this.importData();
    }

    public void importData() {
        this.runImport(DataConfiguration.getDmlData());
    }

}


