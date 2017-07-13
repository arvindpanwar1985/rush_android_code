package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.LocationRequest;
import com.hoffmans.rush.listners.ApiCallback;

public class UpdateCurentLocation extends IntentService {

    private static final String ACTION_LOCATION = "com.hoffmans.rush.services.action.location_update";
    private static final String TAG             = UpdateCurentLocation.class.getCanonicalName();
    private static final String KEY_AUTH        = "auth";
    private static final String KEY_LAT         = "lat";
    private static final String KEY_LNG         = "lng";
    public UpdateCurentLocation() {
        super("UpdateCurentLocation");
    }


    public static void startLocationUpdate(Context context, String auth, String latitude,String longitude){
        Intent intent = new Intent(context, UpdateCurentLocation.class);
        intent.setAction(ACTION_LOCATION);
        intent.putExtra(KEY_AUTH, auth);
        intent.putExtra(KEY_LAT, latitude);
        intent.putExtra(KEY_LNG, longitude);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent){
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOCATION.equals(action)) {
                final String auth     = intent.getStringExtra(KEY_AUTH);
                final String latitude = intent.getStringExtra(KEY_LAT);
                final String logitude = intent.getStringExtra(KEY_LNG);
                //Update user location request
                //updateUserLocation(auth,"9.0971449","-79.5120629");
                updateUserLocation(auth,latitude,logitude);
            }
       }
    }
     /**
     * api call for update user location
     * @param auth auth token
     * @param latitude
     * @param longitude
     */
    private  void updateUserLocation( String auth, String latitude,String longitude){
        LocationRequest request=new LocationRequest();
        request.updateUserLocation(auth, latitude, longitude, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                if(body!=null) {
                    Log.v(TAG, body.getMessage());
                }
            }
            @Override
            public void onRequestFailed(String message) {
                Log.v(TAG,message);
            }
        });
    }
 }
