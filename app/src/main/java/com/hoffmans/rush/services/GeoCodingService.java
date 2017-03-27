package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.hoffmans.rush.R;
import com.hoffmans.rush.http.ApiInterface;
import com.hoffmans.rush.model.FetchAddressEvent;
import com.hoffmans.rush.utils.ApiConfig;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeoCodingService extends IntentService {

    private static final String ACTION_PLACE_ID = "com.hoffmans.rush.services.action.placeId";

    private String MESSAGE="Unable to find Geocode data.";
    private static Retrofit retrofit;
    private FetchAddressEvent fetchAddressEvent=new FetchAddressEvent();
    private static final String EXTRA_LAT    = "lat";
    private static final String EXTRA_LNG    = "lng";
    private static final String KEY_LATLNG   = "latlng";
    private static final String KEY_API_KEY  = "key";
    private static final String KEY_STATUS   = "status";
    private static final String KEY_OK       = "OK";
    private static final String KEY_RESULT   = "results";
    private static final String KEY_PLACE_ID = "place_id";

    public GeoCodingService() {
        super("GeoCodingService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void getInstance(Context context, LatLng latLng) {
        Intent intent = new Intent(context, GeoCodingService.class);
        intent.setAction(ACTION_PLACE_ID);
        intent.putExtra(EXTRA_LAT, latLng.latitude);
        intent.putExtra(EXTRA_LNG, latLng.longitude);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PLACE_ID.equals(action)) {
                final double latitude  =  intent.getDoubleExtra(EXTRA_LAT,0.0);
                final double longitude =  intent.getDoubleExtra(EXTRA_LNG,0.0);
                if(latitude!=0.0 && longitude!=0.0) {
                    getPlaceID(latitude, longitude);
                }else{
                    //TODO throw error
                }
            }
        }
    }
    /**
     * get placeId
     * @param latitude
     * @param longitude
     */
    private void getPlaceID(double latitude, double longitude) {

        StringBuilder latLngBuilder=new StringBuilder(String.valueOf(latitude));
        latLngBuilder.append(",").append(String.valueOf(longitude));
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(KEY_LATLNG,latLngBuilder.toString());
        hashMap.put(KEY_API_KEY,getString(R.string.geo_coding_key));
        getApiInterface().getPlaceID(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody=response.body();
                if(response.isSuccessful()) {
                    try {
                        String res = responseBody.string();
                        parseGeoCodeData(res);
                    }catch (IOException e){
                        setFetchAddressstatus(MESSAGE,false);
                    }
                }else{
                    setFetchAddressstatus(MESSAGE,false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                fetchAddressEvent.setSucess(false);
                fetchAddressEvent.setMessage(t.getMessage());
                EventBus.getDefault().post(fetchAddressEvent);
            }
        });
    }

    /**
     *
     * @param response response from geocoding api
     */
    private void parseGeoCodeData(String response){
        try {
            JSONObject object = new JSONObject(response);
            if (object.getString(KEY_STATUS).equals(KEY_OK)) {
                JSONArray  jsonArray = object.getJSONArray(KEY_RESULT);
                JSONObject firstJsonObject=jsonArray.getJSONObject(0);
                if(firstJsonObject!=null){
                    String placeID=firstJsonObject.getString(KEY_PLACE_ID);
                    if(placeID!=null && !TextUtils.isEmpty(placeID)){
                        //call new intent service to get address component detail.
                        BuildAddressService.buildAddresses(GeoCodingService.this,placeID,true         );
                    }else{
                        setFetchAddressstatus(MESSAGE,false);
                    }
                    Log.e("placeID",placeID);
                }else{
                    setFetchAddressstatus(MESSAGE,false);
                }
            }else{
                setFetchAddressstatus(MESSAGE,false);
            }
        } catch (Exception e) {
            setFetchAddressstatus(MESSAGE,false);
        }
    }
    /**
     * set the event using EventBus
     * @param message
     * @param isSuccess
     */
    private void setFetchAddressstatus(String message,boolean isSuccess){
        fetchAddressEvent.setSucess(isSuccess);
        fetchAddressEvent.setMessage(message);
        EventBus.getDefault().post(fetchAddressEvent);
    }


    private ApiInterface getApiInterface(){

        if(retrofit!=null){
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            return apiInterface;
        }else {
            OkHttpClient.Builder okHttpClient =
                    new OkHttpClient.Builder();
            retrofit = new Retrofit.Builder().baseUrl(ApiConfig.PLACE_BASE_URL)
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.connectTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            return apiInterface;
        }
    }
}
