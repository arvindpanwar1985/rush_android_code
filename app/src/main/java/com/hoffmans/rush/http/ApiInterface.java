package com.hoffmans.rush.http;

import com.hoffmans.rush.model.AddFavouriteBody;
import com.hoffmans.rush.model.EstimateServiceParams;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

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


    @Multipart
    @POST("/api/user/profile_update")
    Call<ResponseBody> updateUserWithImage(@Header("Authorization")String authorization,@PartMap() Map<String, RequestBody> requestBodyMap,@Part MultipartBody.Part file);


    @GET("/api/currencies/all_currency_symbols")
    Call<ResponseBody> getCurrency();

    @GET("/api/creditcard/list_cards")
    Call<ResponseBody> getCardList(@Header("Authorization")String authorization);


    @POST("/api/services/estimate_service")
    Call<ResponseBody> estimateService(@Header("Authorization")String authorization , @Body EstimateServiceParams estimateServiceParams);

    @POST("/api/services/add_service")
    Call<ResponseBody> confirmService(@Header("Authorization")String authorization , @Body EstimateServiceParams estimateServiceParams);

    @POST("/api/address/create_fav_address")
    Call<ResponseBody> createfavoriteAddress(@Header("Authorization")String authorization , @Body AddFavouriteBody addFavouriteBody);

    @GET("/api/address/my_favorite")
    Call<ResponseBody> getMyFavourite(@Header("Authorization")String authorization);

    @FormUrlEncoded
    @POST("/api/address/mark_favorite_unfavorite")
    Call<ResponseBody> markFavUnfav(@Header("Authorization")String authorization,@Field("address_id") String password);


    @GET("/api/services/my_services")
    Call<ResponseBody> getRecords(@Header("Authorization")String authorization,@QueryMap(encoded = true) Map<String, String> params);


    @GET("/maps/api/geocode/json")
    Call<ResponseBody> getPlacesDetails(@QueryMap(encoded = true) Map<String, String> params);

    @GET("/api/creditcard/delete_card")
    Call<ResponseBody> deleteCard(@Header("Authorization")String authorization,@QueryMap(encoded = true) Map<String, String> params);

}
