package com.hoffmans.rush.utils;

/**
 * Created by devesh on 22/12/16.
 */

public class ApiConfig {


    public static final String PLACE_BASE_URL="https://maps.googleapis.com/";
    private static final String BASE_URL = "http://35.161.140.168";
    private static final String QA_URL = "http://qa.rushpanama.net";
    public static String getBaseUrl(){
        return BASE_URL;
    }
   // public static final String DEV_BASE_URL="http://192.168.1.150:3000"; //pravesh's IP
   // public static final String DEV_BASE_URL="http://192.168.3.210:3000"; //chayan's IP
     public static final String DEV_BASE_URL="http://192.168.3.226:3000";   //shweta ip
    public static String getdevBaseUrl(){
        return DEV_BASE_URL;
    }
    public static String getTestUrl(){
        return QA_URL;
    }
}
