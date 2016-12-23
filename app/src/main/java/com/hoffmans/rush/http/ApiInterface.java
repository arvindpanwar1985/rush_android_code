package com.hoffmans.rush.http;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    @POST("/apies/api.php")
    Call<ResponseBody> getArticleDetail(@Field("api") String name);


    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);
    @FormUrlEncoded
    @POST("/api/user/register")
    Call<ResponseBody> createUser(@Field("user")String userdetail);


}
