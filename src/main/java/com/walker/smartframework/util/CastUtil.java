package com.walker.smartframework.util;


import org.apache.commons.lang3.StringUtils;

/**
 * Created by wk on 2015/11/25.
 */
public final class CastUtil {
    // String
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    public static String castString(Object obj, String defaultValue){
        return obj != null ? String.valueOf(obj) : defaultValue;
    }
    //double
    public static double castDouble(Object obj){
        return castDouble(obj,0);
    }
    public static double castDouble(Object obj, double defaultValue){
        double value = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    value = Double.parseDouble(strValue);
                }catch(NumberFormatException e){
                    value = defaultValue;
                }
            }
        }
        return value;
    }
    //long
    public static long castLong(Object obj){
        return castLong(obj,0);
    }

    public static long castLong(Object obj, long defaultValue) {
        long value = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    value = Long.parseLong(strValue);
                } catch (NumberFormatException e) {

                    value = defaultValue;
                }
            }
        }
        return value;
    }

    //int
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }
    public static int castInt(Object obj, int defaultValue){
        int value = defaultValue;
        if(obj != null){
            String strValue = castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try {
                    value = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }
    //boolean
    public static boolean castBoolean (Object obj) {
        return castBoolean(obj, false);
    }
    public static boolean castBoolean(Object obj,boolean defaultValue) {
        boolean value = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)) {
                try {
                    value = Boolean.parseBoolean(strValue);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }
}
