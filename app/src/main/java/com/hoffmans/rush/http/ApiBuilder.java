package com.hoffmans.rush.http;

import com.hoffmans.rush.http.apiEndPoint.GetRequest;
import com.hoffmans.rush.http.apiEndPoint.PostRequest;
import com.hoffmans.rush.http.apiEndPoint.PutRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by LOGAN on 5/1/2016.
 */
public class ApiBuilder {

    public static Retrofit retrofit;
    private static String  BASE_URL="http://httpbin.org/";
    static PostRequest postRequest;
    static GetRequest getRequest;
    static PutRequest putRequest;



    private static OkHttpClient.Builder okHttpClient =
            new OkHttpClient.Builder();


    public  static PostRequest  getPostRequestInstance(){
        setLogInterCeptor();
        if(retrofit!=null){
            postRequest=retrofit.create(PostRequest.class);
            return postRequest;
        }else{
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
             // set the okhttpclient and add default connect and read timepouts
            .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
            .build();
            // Create an instance of our GitHub API interface.
             postRequest=retrofit.create(PostRequest.class);
             return postRequest;
            }

        }


    public  static GetRequest  getGetRequestInstance(){
        setLogInterCeptor();
        if(retrofit!=null){
            getRequest=retrofit.create(GetRequest.class);


            return getRequest;
        }else{
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
                    .build();
            // Create an instance of our GitHub API interface.

            getRequest=retrofit.create(GetRequest.class);
            return getRequest;
        }


    }




    public  static PutRequest  getPutRequestInstance(){

        setLogInterCeptor();
        if(retrofit!=null){
            putRequest=retrofit.create(PutRequest.class);
            return putRequest;
        }else{
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
                    .build();
            // Create an instance of our GitHub API interface.
            putRequest=retrofit.create(PutRequest.class);
            return putRequest;
        }

    }


    /**
     * set log interceptor for logging the network response
     */

    private static  void setLogInterCeptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(interceptor).build();
    }


    /**
     *
     * @return Retrofit Instance
     */
    public static Retrofit getRetrofitInstance(){

        if(retrofit!=null){

            return retrofit;

        }else{
            return new Retrofit.Builder().baseUrl(BASE_URL)
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
                    .build();
            // Create an instance of our GitHub API interface.

        }

    }
}



