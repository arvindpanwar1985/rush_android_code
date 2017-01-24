package com.hoffmans.rush.http;

import com.hoffmans.rush.utils.ApiConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LOGAN on 5/1/2016.
 */
public class ApiBuilder {

    public static Retrofit retrofit;
    static ApiInterface apiInterface;



    private static OkHttpClient.Builder okHttpClient =
            new OkHttpClient.Builder();


    public  static ApiInterface  createApiBuilder(){
        setLogInterCeptor();
        if(retrofit!=null){
            apiInterface=retrofit.create(ApiInterface.class);
            return apiInterface;
        }else{
            retrofit = new Retrofit.Builder().baseUrl(ApiConfig.getdevBaseUrl())
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface=retrofit.create(ApiInterface.class);
            return apiInterface;
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
            return new Retrofit.Builder().baseUrl(ApiConfig.getdevBaseUrl())
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // Create an instance of our GitHub API interface.
        }

    }
}



