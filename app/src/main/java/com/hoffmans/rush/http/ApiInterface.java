package com.hoffmans.rush.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by devesh on 23/12/16.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/auth/authenticate")
    Call<ResponseBody> login(@Field("login") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("/api/user/oauthRegister")
    Call<ResponseBody> loginViaSocialNetwork(@Field("uid") String uid,@Field("first_name") String password,@Field("last_name") String last_name,@Field("email") String email,@Field("provider") String provider,@Field("profile_url") String picture,@Field("uuid") String uuid,@Field("type") String type,@Field("time_zone") String time_zone);


    @Multipart
    @POST("/api/user/register")
    Call<ResponseBody> createUser(@PartMap() Map<String, RequestBody> requestBodyMap,@Part MultipartBody.Part file);




    @POST("/api/user/profile_update")
    Call<ResponseBody> updateUser(@Header("Authorization")String authorization ,@Body HashMap<String,Object> userdetail);


    @FormUrlEncoded
    @POST("/api/user/forgot_password")
    Call<ResponseBody> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/creditcard/addCard")
    Call<ResponseBody> addCreditCard(@Header("Authorization")String authorization,@Field("payment_method_nonce") String payment_method_nonce);





}
