package com.hoffmans.rush.http.apiEndPoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by devesh on 16/12/16.
 */

public interface PostRequest {

    @FormUrlEncoded
    @POST("/apies/api.php")
    Call<ResponseBody> getArticleDetail(@Field("api") String name);
}
