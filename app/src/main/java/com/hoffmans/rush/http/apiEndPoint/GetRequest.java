package com.hoffmans.rush.http.apiEndPoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by devesh on 16/12/16.
 */

public interface GetRequest  {

    @GET("/ip")
    Call<ResponseBody> getArticleDetail();
}
