package com.blogspot.fravalle.data;

import com.blogspot.fravalle.core.DataConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

public class UrlDomain {
    //iwdomainname, iwtitle, iwurl, iwhashttps,

    private static Integer univoqueId = 1;
    private static Integer univoqueCatId = 0;

    private Integer id;
    private String iwCategoryName;
    private Integer iwCategoryId;
    private String iwDomainName;
    private String iwTitle;
    private String iwUrl;

    private Integer depthLevel;
    private Short iwhttpcode;

    private Vector<String> routes = new Vector<String>();


    public Short getIwhttpcode() {
        return iwhttpcode;
    }

    public void setIwhttpcode(Short iwhttpcode) {
        this.iwhttpcode = iwhttpcode;
    }

    public Integer getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(Integer depthLevel) {
        this.depthLevel = depthLevel;
    }

    public Integer getIwCategoryId() {
        return iwCategoryId;
    }

    public void setIwCategoryId(Integer iwCategoryId) {
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

    public static UrlDomain parseUrlDomainBoomark(String sourceCatName, String bookmarkSource){
        UrlDomain urlDomain = new UrlDomain();
        urlDomain.setIwhttpcode(Integer.valueOf(200).shortValue());
        String rawCatName1 = sourceCatName.indexOf('>')!=-1 ? cutterLast("\">", "<", 0, sourceCatName) : sourceCatName;
        //System.out.println("CATEGORY FOUND=["+rawCatName1+"]");
        urlDomain.setIwCategoryName(rawCatName1);

        String rawUrl1 = cutter("HREF=\"", "\"", 1, bookmarkSource);
        String[] urlParts = rawUrl1.split("/");
        //urlDomain.setIwDomainName(urlParts[2]);
        if (rawUrl1.indexOf("//")!=-1) {
            urlDomain.setIwDomainName(urlParts[2]);
        } else {
            String urlOut = "";
            for (String s: urlParts) {
                urlOut+="/"+s;
            }
            urlDomain.setIwDomainName(urlOut);
        }

        String urlString = "";
        int i=0;
        for (String s : urlParts) {
            if (i>2) {
                urlString += "/" + s;
            }
            i++;
        }
        urlDomain.setIwUrl(urlString);

        String rawUrl2 = cutterLast("\">", "<", 0, bookmarkSource);

        urlDomain.setIwTitle(rawUrl2);

        return urlDomain;
    }

    public static UrlDomain parseUrlDomainUrl(String sourceCatName, String urlSource, Integer currentDepthLevel){
        UrlDomain urlDomain = new UrlDomain();

        urlDomain.setIwhttpcode(Integer.valueOf(200).shortValue());
        urlDomain.setDepthLevel(currentDepthLevel);

        String rawUrl1 = cutter("href=\"", "\"", 0, urlSource);
        //System.out.println("RAW="+rawUrl1);
        String[] urlParts = rawUrl1.split("/");
        if (rawUrl1.indexOf("//")!=-1) {
            urlDomain.setIwDomainName(urlParts[2]);
        } else {
            //BASE SAVED DOMAIN
            urlDomain.setIwDomainName(DataConfiguration.SIMPLE_STRING);
        }
        String urlString = "";
        int i=0;
        for (String s : urlParts) {
            if (i>2) {
                urlString += "/" + s;
            }
            i++;
        }
        urlDomain.setIwUrl(urlString);

        //String rawUrl2 = cutter("\">", "<", 0, urlSource);
        //urlDomain.setIwTitle(rawUrl2);
        urlDomain.setIwTitle("Default title");

        //String rawCatName1 = sourceCatName.indexOf('>')!=-1 ? cutter("\">", "<", 0, sourceCatName) : sourceCatName;
        urlDomain.setIwCategoryId(univoqueCatId);
        urlDomain.setIwCategoryName(urlDomain.getIwDomainName());

        return urlDomain;
    }

    public static String cutter(String from, String to, int offset, String source) {
        int cutFrom = source.indexOf(from)+from.length();
        int cutTo = 0;
        String firstCut = source;
        if (cutFrom!=-1) {
            firstCut = source.substring(cutFrom);
            cutTo = firstCut.indexOf(to);
        }
        /*
        if (cutTo==-1)
            return firstCut;*/
        //int cutTo = source.lastIndexOf(to);
        //System.out.println("CUT_SOURCE:"+source);
        //System.out.println("CUT_FROM:"+cutFrom);
        //System.out.println("CUT_TO:"+cutTo);
        //System.out.println("SOURCE CUTTER:"+source+"\nFROM="+from+"["+cutFrom+"]\nFIRST="+firstCut);
        String rawUrl = firstCut.substring(0, firstCut.indexOf(to));
        //System.out.println("\nRAWURL="+rawUrl);
        return rawUrl;
    }

    public static String cutterLast(String from, String to, int offset, String source) {
        int cutFrom = source.indexOf(from);
        int cutTo = source.lastIndexOf(to);
        //System.out.println("SOURCE CUTTER LAST:"+source+"\nFROM="+from+"["+cutFrom+"]");

        //int cutTo = source.lastIndexOf(to);
        //System.out.println("CUT_SOURCE:"+source);
        //System.out.println("CUT_FROM:"+cutFrom);
        //System.out.println("CUT_TO:"+cutTo);
        String rawUrl = source.substring(cutFrom+from.length(), cutTo-offset);
        return rawUrl;
    }

}