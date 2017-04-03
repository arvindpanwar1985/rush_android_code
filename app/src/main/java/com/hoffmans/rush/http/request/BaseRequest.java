package com.hoffmans.rush.http.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoffmans.rush.http.ApiBuilder;
import com.hoffmans.rush.http.ApiInterface;

/**
 * Created by devesh on 23/12/16.
 */

public class BaseRequest {

    private Gson gson= new GsonBuilder().create();
    public String DATA       ="data";
    public String MESSAGE    ="message";
    public String SUCCESS    ="success";

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
}
