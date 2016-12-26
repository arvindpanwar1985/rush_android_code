package com.hoffmans.rush.http;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by devesh on 23/12/16.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/auth/authenticate")
    Call<ResponseBody> login(@Field("login") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("/api/user/oauthRegister")
    Call<ResponseBody> loginViaSocialNetwork(@Field("uid") String uid,@Field("First_name") String password,@Field("last_name") String last_name,@Field("email") String email,@Field("provider") String provider);


    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

    @POST("/api/user/register")
    Call<ResponseBody> createUser(@Body HashMap<String,Object> userdetail);



}
