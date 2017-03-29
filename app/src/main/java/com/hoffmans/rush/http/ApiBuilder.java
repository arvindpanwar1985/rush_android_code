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
    public static Retrofit retrofit;
    static ApiInterface apiInterface;
    private static OkHttpClient.Builder okHttpClient =
            new OkHttpClient.Builder();
    /**
     * create ApiInterface
     * @return ApiInterface
     */
    public  static ApiInterface  createApiBuilder(){
         setLogInterCeptor();
         if(retrofit!=null){
             return apiInterface=retrofit.create(ApiInterface.class);
         }else {
             retrofit = new Retrofit.Builder().baseUrl(ApiConfig.getBaseUrl())
                     // set the okhttpclient and add default connect and read timepouts
                     .client(okHttpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).build())
                     .addConverterFactory(GsonConverterFactory.create())
                     .build();
             apiInterface = retrofit.create(ApiInterface.class);
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
        setLogInterCeptor();
        if(retrofit!=null){
            return retrofit;
        }else {
            return new Retrofit.Builder().baseUrl(ApiConfig.getBaseUrl())
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
     }
}
