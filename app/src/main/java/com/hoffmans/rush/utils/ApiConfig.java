package com.hoffmans.rush.utils;

/**
 * Created by devesh on 22/12/16.
 */

public class ApiConfig {


    private static final String BASE_URL = "http://54.86.93.44/triibe/api/";
    public static String getBaseUrl(){
        return BASE_URL;
    }
    public static final String DEV_BASE_URL="http://192.168.3.226:3001";
    public static String getdevBaseUrl(){
        return DEV_BASE_URL;
    }
}
