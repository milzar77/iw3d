package com.blogspot.fravalle.data.utils;

import java.math.BigDecimal;

public class LibSqlUtils {

    public static String formatForSqlInsert(Object asType) {
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

    public static String buildSqlInsertFromEnum(Class clz) {
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

}
