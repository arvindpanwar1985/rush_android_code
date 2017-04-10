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

    private static final int CONNECT_TIME_OUT =15;// in seconds
    private static final int READ_TIME_OUT    =20;// in seconds
    private static Retrofit retrofit;
    private static ApiInterface apiInterface;
    private static OkHttpClient.Builder okHttpClient =new OkHttpClient.Builder();
    private static  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    /**
     * create ApiInterface
     * @return ApiInterface
     */
    public  static ApiInterface  createApiBuilder(){
         // set logging interceptor
         setLogInterCeptor();
         if(retrofit!=null){
             return apiInterface=retrofit.create(ApiInterface.class);
         }else {
             retrofit = new Retrofit.Builder()
                     .baseUrl(ApiConfig.getdevBaseUrl())
                     .client(okHttpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS) //default  connect timeout
                     .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).build())                  //default  read timeout
                     .addConverterFactory(GsonConverterFactory.create())                     // add converter factory
                     .build();
             return retrofit.create(ApiInterface.class);
         }
    }

    /**
     * set log interceptor for logging the network response
     */
    private static  void setLogInterCeptor(){
        if(interceptor==null){
            interceptor=new HttpLoggingInterceptor();
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.addInterceptor(interceptor).build();
    }
    /**
     *
     * @return Retrofit Instance
     */
    public static Retrofit getRetrofitInstance(){
        //set log interceptor
        setLogInterCeptor();
        if(retrofit!=null){
            return retrofit;
        }else {
            return new Retrofit.Builder()
                    .baseUrl(ApiConfig.getdevBaseUrl())
                    .client(okHttpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS) //default  connect timeout
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).build())                  //default  read timeout
                    .addConverterFactory(GsonConverterFactory.create())                     // add converter factory
                    .build();
        }
     }

}

