package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoffmans.rush.http.ApiInterface;
import com.hoffmans.rush.model.AddressComponent;
import com.hoffmans.rush.model.FetchAddressEvent;
import com.hoffmans.rush.model.PlacesData;
import com.hoffmans.rush.utils.ApiConfig;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
 *
 * helper methods.
 */
public class BuildAddressService extends IntentService {

    private static final String KEY_API_KEY                   ="key";
    private static final String MESSAGE                       ="Unable to find Geocode data.";
    private static final String KEY_STATUS                    ="status";
    private static final String KEY_OK                        ="OK";
    private static final String KEY_PLACE_ID                  ="placeid";
    private static final String KEY_RESULT                    ="result";
    private static final String TYPE_COUNTRY                  ="country";
    private static final String TYPE_LOCALITY                 ="locality";
    private static final String KEY_FORMATTED_ADDRESS         ="formatted_address";
    private static final String TYPE_ADMIN_AREA               ="administrative_area_level_1";
    private static final String ACTION_BUILD_ADDRESS          = "com.hoffmans.rush.services.action.BUILD_ADDRESS";
    private static final String KEY_LOCATION                  ="location";
    private static final String KEY_LAT                       ="lat";
    private static final String KEY_LNG                       ="lng";
    private static final String KEY_DRAG                      ="marker_drag";
    private Gson gson=new GsonBuilder().create();
    private static Retrofit retrofit;
    private FetchAddressEvent fetchAddressEvent=new FetchAddressEvent();
    private boolean mIsDrag;
    public BuildAddressService() {
        super("BuildAddressService");
    }

    /**
     *
     * @param context
     * @param placeId placeId of particular location
     * @param isDraggable
     */
    public static void buildAddresses(Context context, String placeId,boolean isDraggable) {
        Intent intent = new Intent(context, BuildAddressService.class);
        intent.setAction(ACTION_BUILD_ADDRESS);
        intent.putExtra(KEY_PLACE_ID,placeId);
        intent.putExtra(KEY_DRAG,isDraggable);
        context.startService(intent);
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_BUILD_ADDRESS.equals(action)) {
                final String plcaeId=intent.getStringExtra(KEY_PLACE_ID);
                mIsDrag =intent.getBooleanExtra(KEY_DRAG,false);
                handleActionFoo(plcaeId);
            }
        }
    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String placeId) {
        getPlaceDetails(placeId);

    }
    private void getPlaceDetails(String placeId){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(KEY_PLACE_ID,placeId);
        hashMap.put(KEY_API_KEY,"AIzaSyDeE8mZavwOAHWE4Up3WtM6-L7NClaffvY");
        getApiInterface().getPlacesDetails(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    ResponseBody responseBody=response.body();
                    try {
                        parsePlacesResponse(responseBody.string());
                    }catch (IOException e){
                        setFetchAddressstatus(MESSAGE,false);
                    }
                }else{
                    setFetchAddressstatus(MESSAGE,false);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setFetchAddressstatus(t.getMessage(),false);
            }
        });
    }


    private void parsePlacesResponse(String response){
        try {

            JSONObject object = new JSONObject(response);
            if (object.getString(KEY_STATUS).equals(KEY_OK)) {
                JSONObject json = object.getJSONObject(KEY_RESULT);
                PlacesData placesData = gson.fromJson(json.toString(), PlacesData.class);
                List<AddressComponent> addressComponentList = placesData.getAddressComponents();
                String country = "", state = "", city = "";
                for (AddressComponent addressComponent : addressComponentList) {
                    for (String type : addressComponent.getTypes()) {
                        if (type.equals(TYPE_COUNTRY)) {
                            country = addressComponent.getLongName();
                        }
                        if (type.equals(TYPE_ADMIN_AREA)) {
                            state = addressComponent.getLongName();
                        }
                        if (type.equals(TYPE_LOCALITY)) {
                            city = addressComponent.getLongName();
                        }
                    }
                }
                JSONObject locationObject=json.getJSONObject("geometry").getJSONObject(KEY_LOCATION);
                if(locationObject!=null) {
                    fetchAddressEvent.setLat(locationObject.getDouble(KEY_LAT));
                    fetchAddressEvent.setLng(locationObject.getDouble(KEY_LNG));
                }
                String formatted_Address=json.getString(KEY_FORMATTED_ADDRESS);
                fetchAddressEvent.setStreetAddress(formatted_Address);
                fetchAddressEvent.setCity(city);
                fetchAddressEvent.setDrag(mIsDrag);

                fetchAddressEvent.setCountry(country);
                fetchAddressEvent.setState(state);
                fetchAddressEvent.setSucess(true);
                Log.e("place data","Country :"+country+" state: "+state+" city :"+city);
                EventBus.getDefault().post(fetchAddressEvent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(retrofit!=null){
            retrofit=null;
        }
    }
}
