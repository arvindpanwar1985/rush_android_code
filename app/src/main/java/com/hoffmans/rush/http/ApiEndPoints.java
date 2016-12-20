package com.hoffmans.rush.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LOGAN on 5/1/2016.
 */
public interface ApiEndPoints {


    @FormUrlEncoded
    @POST("/apies/api.php")
    Call<ResponseBody> getArticleDetail(@Field("api") String name);

}

