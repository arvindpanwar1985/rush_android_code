package com.hoffmans.rush.http.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoffmans.rush.http.ApiBuilder;
import com.hoffmans.rush.http.ApiInterface;

import java.util.Locale;

/**
 * Created by devesh on 23/12/16.
 */

public class BaseRequest {

    private Gson gson= new GsonBuilder().create();
    public String DATA              ="data";
    public String MESSAGE           ="message";
    public String SUCCESS           ="success";
    public String SPANISH_MESSAGE   ="message1";
    /**
     *
     * @return ApiInterface
     */
    public  ApiInterface getAPIClient(){

        return  ApiBuilder.createApiBuilder();

    }

    public Gson getGsonBuilder(){
        if(gson==null) {
            gson = new GsonBuilder().create();
        }
        gson.serializeNulls();
        return gson;

    }

    /**
     * parse message based on language select
     * @param engMessage
     * @param spaMessaage
     * @return
     */
    public  String parseMessageUsingLocale(String engMessage,String spaMessaage){
       String locale= Locale.getDefault().getLanguage();
        if(locale.equals("es")){
            return  spaMessaage;
        }else {
            return engMessage;
        }
    }
}
