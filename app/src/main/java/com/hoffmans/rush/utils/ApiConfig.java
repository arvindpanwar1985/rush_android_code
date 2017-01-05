package com.hoffmans.rush.utils;

/**
 * Created by devesh on 22/12/16.
 */

public class ApiConfig {


    private static final String BASE_URL = "http://35.161.140.168";
    public static String getBaseUrl(){
        return BASE_URL;
    }
    public static final String DEV_BASE_URL="http://192.168.3.36:3000";
    public static String getdevBaseUrl(){
        return DEV_BASE_URL;
    }
}
