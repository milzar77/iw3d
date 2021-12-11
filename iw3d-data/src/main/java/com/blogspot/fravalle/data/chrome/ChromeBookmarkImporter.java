package com.blogspot.fravalle.data.chrome;

import com.blogspot.fravalle.core.DataConfiguration;
import com.blogspot.fravalle.data.AProgressRunner;
import com.blogspot.fravalle.data.ProgressMessageBean;


import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * This class is used for downloading stars data from HYG database and transform some column's format
 */
public class ChromeBookmarkImporter extends AProgressRunner {

    private static final String FILE_SOURCE_INPUT = "/tmp/bookmarks_10_12_21.html";

    private static final Integer MAX_HOP_TIME = 10;
    private static final Integer MAX_HOPS = 4;

    private static String inputSourceFile = null;

    private String fName;

    public enum IWEBIPV4 {
        iwid4, iwdomainname, iwurl, iwtitle, iwcategoryname, iwcategoryid, iwhashttps,
    }

    public enum ITEMPROUTESIPV4 {
        itrdomainname, itripaddress, itrhop, itiwid4,
    }

    public enum CHROME_BOOKMARK_IMPORT {
        href, add_date, icon,
    }

    private Long sleepTime = 100L;

    private StringBuffer sbContent = new StringBuffer(1024);

    public static HashMap<String, Integer> categories = new LinkedHashMap<String, Integer>();

    @Override
    public ProgressMessageBean executeProgress(int SWITCH_INDEX) {

        ProgressMessageBean pmbCurrent = null;

        System.err.printf("STEP NUMBER #%1$s\n", SWITCH_INDEX);

        return this.getOperationInProgress(SWITCH_INDEX);
    }


    @Override
    public Boolean skipThreadSleep() {
        return this.sleepingTime() == SKIP_THREAD_SLEEP;
    }

    @Override
    public Long sleepingTime() {
        return sleepTime;
    }

    @Override
    public Boolean hasDynamicSteps() {
        return hasDynamicSteps;
    }

    //public Vector<Runnable> runnableVector = new Vector<>();
    private String lineImport;

    private Vector<UrlDomain> domains = new Vector<UrlDomain>();

    public Integer step1_importBookmarks() throws IOException {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DataConfiguration.getDmlData(), false);

            if (!DataConfiguration.isDerbyCached()) {

                int limitCounter = 1;

                String dmlImport1 = this.buildSqlInsertFromEnum( IWEBIPV4.class );


                Reader in0 = new FileReader(this.inputSourceFile);


                //filling categories
                String currentCategoryName = null;
                BufferedReader br0 = new BufferedReader(in0);
                while ((lineImport=br0.readLine()) != null) {

                    if (lineImport!=null && !lineImport.contains("<H3") && lineImport.contains("<DT>")) {
                        if (lineImport.contains("dodiff")) {
                            continue;
                        } else {

                        }
                    } else if (lineImport!=null && lineImport.contains("<H3")) {
                        if (currentCategoryName==null) {
                            currentCategoryName = "FV";
                            categories.put(currentCategoryName, categories.size()+1 );
                        } else {
                            String rawCatName1 = lineImport.indexOf('>')!=-1 ? UrlDomain.cutter("\">", "<", 0, lineImport) : lineImport;
                            currentCategoryName = rawCatName1;
                            categories.put(currentCategoryName, categories.size()+1 );
                        }
                    }
                }
                //br0.close();
                System.out.println("CATEGORIES: " + categories);

                Reader in1 = new FileReader(this.inputSourceFile);
                BufferedReader br1 = new BufferedReader(in1);
                StringBuffer sbContent = new StringBuffer(20000000);

                while ((lineImport=br1.readLine()) != null) {

                    if (lineImport!=null && !lineImport.contains("<H3") && lineImport.contains("<DT>")) {
                        if (!lineImport.contains("dodiff")) {
                            UrlDomain urlDomain = UrlDomain.parseUrlDomainBoomark(currentCategoryName, lineImport);
                            if ((limitCounter++)>DataConfiguration.getMaxImportRows()) {
                                break;
                            }

                            String[] args = new String[IWEBIPV4.values().length];
                            args[0] = this.formatForSqlInsert(urlDomain.getId());
                            args[1] = this.formatForSqlInsert(urlDomain.getIwDomainName());
                            args[2] = this.formatForSqlInsert(urlDomain.getIwUrl());
                            args[3] = this.formatForSqlInsert(urlDomain.getIwTitle());
                            args[4] = this.formatForSqlInsert(urlDomain.getIwCategoryName());
                            args[5] = this.formatForSqlInsert(categories.get(urlDomain.getIwCategoryName()));
                            args[6] = "false";
                            fos.write( String.format(dmlImport1+"\n", args).getBytes(Charset.defaultCharset()) );
                            fos.flush();
                            domains.add(urlDomain);

                        }
                    } else if (lineImport!=null && lineImport.contains("<H3")) {
                        if (currentCategoryName==null) {
                            currentCategoryName = "FV";
                        } else {
                            currentCategoryName = lineImport;
                        }
                    }
                }
                br0.close();
                in0.close();
                br1.close();
                in1.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos!=null) {
                fos.close();
            }
        }
        nextIterationStep = 1;
        return nextIterationStep;
    }

    public Integer step1_importTraceroute() throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DataConfiguration.getDmlData(), true);

            if (!DataConfiguration.isDerbyCached()) {

                //clean old files
                int limitCounter = 1;//DataConfiguration.getMaxImportRows();

                for (UrlDomain dom : domains) {

                    ChromeBookmarkImporter.retrieveNetStats(dom);


                    Reader in2 = new FileReader("/tmp/traceroute.log");
                    BufferedReader br2 = new BufferedReader(in2);
                    StringBuffer sbContent2 = new StringBuffer(20000000);
                    boolean startImport = false;

                    while ((lineImport=br2.readLine()) != null) {
                        if (lineImport != null && lineImport.contains("su un massimo")) {
                            startImport = true;
                        } else if (startImport && lineImport != null) {
                            if (!lineImport.trim().equals("")) {
                                if (lineImport.contains("Traccia completata.")) {
                                    startImport = false;
                                    break;
                                }
                                String routePath = this.parseTracerouteIp(lineImport);
                                dom.addRoute(routePath);
                            }

                        }
                    }

                    br2.close();
                    in2.close();

                    if ((limitCounter++)>DataConfiguration.getMaxImportRows()) {
                        break;
                    }

                }

                System.err.printf("DOMAINS: %1$s\n", domains);

                limitCounter = 1;

                for (UrlDomain dom : domains) {

                    if ((limitCounter++) > DataConfiguration.getMaxImportRows()) {
                        break;
                    }

                    System.err.printf("PATHS FOR %1$s : %2$s\n", dom.getIwDomainName(), dom.getRoutes());

                    String dmlImport2 = this.buildSqlInsertFromEnum( ITEMPROUTESIPV4.class );

                    int cnt = 1;
                    for (String ip : dom.getRoutes()) {
                        String[] args = new String[ITEMPROUTESIPV4.values().length];
                        args[0] = this.formatForSqlInsert(dom.getIwDomainName());
                        args[1] = this.formatForSqlInsert(ip);
                        args[2] = this.formatForSqlInsert(cnt);
                        args[3] = this.formatForSqlInsert(dom.getId());
                        fos.write( String.format(dmlImport2+"\n", args).getBytes(Charset.defaultCharset()) );
                        fos.flush();
                        cnt++;
                    }



                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos!=null) {
                fos.close();
            }
        }

        //TODO: remove debugging exit
        //System.exit(1);

        nextIterationStep = 2;
        return nextIterationStep;
    }

    private Integer parseTracerouteHop(String sourceTr) {
        /*int cutStart = !sourceTr.contains("*") ? sourceTr.lastIndexOf("ms") : sourceTr.lastIndexOf("*");
        String s = cutStart > 0 ? sourceTr.substring(cutStart).trim().replaceAll("ms ", "").trim() : "127.0.0.1";*/
        int cutStart = sourceTr.trim().indexOf("  ");
        String s1 = sourceTr.trim().substring(cutStart).trim();
        String s2 = s1.substring(0, s1.indexOf(' ')).trim();
        //if (!s.equals("*")) {}
        System.err.println("DEBUG: <" + s2 + ">");
        return Integer.parseInt(s2);
    }

    private String parseTracerouteIp(String sourceTr) {
        /*int cutStart = !sourceTr.contains("*") ? sourceTr.lastIndexOf("ms") : sourceTr.lastIndexOf("*");
        String s = cutStart > 0 ? sourceTr.substring(cutStart).trim().replaceAll("ms ", "").trim() : "127.0.0.1";*/
        int cutStart = sourceTr.trim().lastIndexOf(' ');
        String s = sourceTr.trim().substring(cutStart);
        return s.trim();
    }

    public ChromeBookmarkImporter() {
        super.steps = 1;
        this.inputSourceFile = FILE_SOURCE_INPUT;

        ProgressMessageBean pmbStep1 = null;
        sleepTime = Long.valueOf(SKIP_THREAD_SLEEP);//TODO: move to ProgressMessageBean constructor
        operationsInProgress.add(pmbStep1 = new ProgressMessageBean(() -> {
            try {
                this.step1_importBookmarks();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }, 1, "Importazione HTML su di un massimo di righe consentite", Boolean.FALSE));

        ProgressMessageBean pmbStep2 = null;
        sleepTime = Long.valueOf(SKIP_THREAD_SLEEP);//TODO: move to ProgressMessageBean constructor
        operationsInProgress.add(pmbStep2 = new ProgressMessageBean(() -> {
            try {
                this.step1_importTraceroute();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }, 2, "Importazione traceroute su di un massimo di righe consentite", Boolean.FALSE));

        this.hasDynamicSteps = Boolean.FALSE;
        this.setSteps(operationsInProgress.size());

    }

    private String buildSqlInsertFromEnum(Class clz) {
        //System.err.println( "ENUM NAME: "+ clz.getSimpleName().toLowerCase());
        Object[] enValues = clz.getEnumConstants();
        /*for (Object o : enValues) {
            System.err.println( "ENUM PROP: <"+ o +">" );
        }*/
        //Enum.valueOf()
        StringBuffer sbColumns = new StringBuffer(256);
        StringBuffer sbValues = new StringBuffer(256);
        sbColumns.append(String.format("INSERT INTO %1$s.%2$s (", "iw3d", clz.getSimpleName().toLowerCase()));
        sbValues.append(" VALUES(");
        int cnt = 0;
        for (Object o : enValues) {
            String endToken = (cnt+1) < enValues.length ? ", " : "";
            sbColumns.append(o.toString() + endToken);
            sbValues.append("%" + (cnt + 1) + "$s" + endToken);
            cnt++;
        }
        sbColumns.append(")");
        sbValues.append(");");
        sbColumns.append(sbValues.toString());

        return sbColumns.toString();
    }


    private void writeImportSqlFile(String schemaName, int totalMaxRows, StringBuffer sbContent) throws IOException {
        //inCsv = new FileReader(fInputFileName);
        //System.err.println("CONTENT:["+sbContent+"]");


    }

    private String formatForSqlInsert(Object asType) {
        if (asType instanceof BigDecimal) {
            return ((BigDecimal) asType).toPlainString();
        } else if (asType instanceof Short||asType instanceof Integer||asType instanceof Float||asType instanceof Long||asType instanceof Double) {
            //with scientific number notation
            return asType.toString();
        } else if (asType instanceof String) {
            return "'"+String.valueOf(asType).replaceAll("'","''")+"'";
        }
        return "0";
    }

    private static void retrieveNetStats(UrlDomain urlDomain) {

        /*try {
            //TODO: usare vecchio site directory o meglio ancora ricerca google per categoria
            // to resolve a domain name from an ip address: nslookup %ipaddress%
            Process p = Runtime.getRuntime().exec(String.format("C:/devapps/os/utils/whois/whois.exe %1$s", urlDomain.getIwDomainName()) );
            BufferedInputStream bis = new BufferedInputStream( p.getInputStream() );
            if (bis!=null) {
                //os.flush();
                DataInputStream diStream = new DataInputStream(bis);
                String line ="";
                StringBuffer errorBuffer = new StringBuffer(512);
                while ( ( line = diStream.readLine() ) != null ) {
                    errorBuffer.append(line + System.getProperty("line.separator"));
                }

                FileWriter fw = new FileWriter( "c:/tmp/whois.log", false );
                fw.write(errorBuffer.toString());
                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //TODO: rimuovere debug exit
        /*if (true) {
            System.err.println("DEBUG EXITING FROM TRACEROUTE LAUNCHER...");
            return;
        }*/

/*
        try {
            //TODO: usare vecchio site directory o meglio ancora ricerca google per categoria
            // to resolve a domain name from an ip address: nslookup %ipaddress%
            String command = String.format("tracert.exe -d -h %1$s -w %2$s %3$s", MAX_HOPS, MAX_HOP_TIME, urlDomain.getIwDomainName());
            System.err.println("Executing command: " + command );
            Process p = Runtime.getRuntime().exec(command);
            BufferedInputStream bis = new BufferedInputStream( p.getInputStream() );
            if (bis!=null) {
                //os.flush();
                DataInputStream diStream = new DataInputStream(bis);
                String line ="";
                StringBuffer errorBuffer = new StringBuffer(512);
                while ( ( line = diStream.readLine() ) != null ) {
                    errorBuffer.append(line + System.getProperty("line.separator"));
                }

                FileWriter fw = new FileWriter( "c:/tmp/traceroute.log", false );
                fw.write(errorBuffer.toString());
                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

}


class UrlDomain {
    //iwdomainname, iwtitle, iwurl, iwhashttps,

    private static Integer univoqueId = 1;
    private static Integer univoqueCatId = 0;

    private Integer id;
    private String iwCategoryName;
    private String iwCategoryId;
    private String iwDomainName;
    private String iwTitle;
    private String iwUrl;
    private Vector<String> routes = new Vector<String>();


    public String getIwCategoryId() {
        return iwCategoryId;
    }

    public void setIwCategoryId(String iwCategoryId) {
        this.iwCategoryId = iwCategoryId;
    }

    public String getIwCategoryName() {
        return iwCategoryName;
    }

    public void setIwCategoryName(String iwCategoryName) {
        this.iwCategoryName = iwCategoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vector<String> getRoutes() {
        return routes;
    }

    public void addRoute(String r) {
        routes.add(r);
    }

    public String getIwDomainName() {
        return iwDomainName;
    }

    public void setIwDomainName(String iwDomainName) {
        this.iwDomainName = iwDomainName;
    }

    public String getIwTitle() {
        return iwTitle;
    }

    public void setIwTitle(String iwTitle) {
        this.iwTitle = iwTitle;
    }

    public String getIwUrl() {
        return iwUrl;
    }

    public void setIwUrl(String iwUrl) {
        this.iwUrl = iwUrl;
    }

    public UrlDomain() {
        setId(univoqueId++);
    }

    protected static UrlDomain parseUrlDomainBoomark(String sourceCatName, String bookmarkSource){
        UrlDomain urlDomain = new UrlDomain();

        String rawCatName1 = sourceCatName.indexOf('>')!=-1 ? cutter("\">", "<", 0, sourceCatName) : sourceCatName;
        System.out.println("CATEGORY FOUND=["+rawCatName1+"]");
        urlDomain.setIwCategoryName(rawCatName1);

        String rawUrl1 = cutter("HREF=\"", " ADD_DATE=", 1, bookmarkSource);
        String[] urlParts = rawUrl1.split("/");
        urlDomain.setIwDomainName(urlParts[2]);
        String urlString = "";
        int i=0;
        for (String s : urlParts) {
            if (i>2) {
                urlString += "/" + s;
            }
            i++;
        }
        urlDomain.setIwUrl(urlString);

        String rawUrl2 = cutter("\">", "<", 0, bookmarkSource);

        urlDomain.setIwTitle(rawUrl2);

        return urlDomain;
    }

    public static String cutter(String from, String to, int offset, String bookmarkSource) {
        int cutFrom = bookmarkSource.indexOf(from);
        int cutTo = bookmarkSource.lastIndexOf(to);

        String rawUrl = bookmarkSource.substring(cutFrom+from.length(), cutTo-offset);
        return rawUrl;
    }

}