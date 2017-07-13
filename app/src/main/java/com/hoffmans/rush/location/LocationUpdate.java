package com.hoffmans.rush.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.services.UpdateCurentLocation;
import com.hoffmans.rush.utils.ApiConfig;
import com.hoffmans.rush.utils.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.hoffmans.rush.location.LocationData.REQUEST_CHECK_SETTINGS;



/**
 * Created by devesh on 11/12/15.
 */
public class LocationUpdate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final long INTERVAL      =60*1000;
    private static final float DISPLACEMENT   =100;
    private static LocationUpdate mLocationUpdate =null;
    private LocationInterface mLocationListener =null;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;
    private EchoWebSocketListener listener;
    private  WebSocket webSocket;
    private okhttp3.OkHttpClient client;
    private Location newLocation;
    private boolean updateAfterFixedInterval;
    private int updateCount;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private int DEFAULT_HEARBEATCOUNT=3;
    private final static String TAG=LocationUpdate.class.getCanonicalName();
    private LocationUpdate(){

    }

    public static LocationUpdate getInstance(){
        if(mLocationUpdate ==null){
            mLocationUpdate =new LocationUpdate();
        }
        return mLocationUpdate;
    }
    public void startLocationUpdate(Context context, LocationInterface locationInterface,boolean updateAfterFixedInterval){
        // Create an instance of GoogleAPIClient.
        this.mContext=context;
        this.mLocationListener=locationInterface;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
                    mGoogleApiClient.connect();
        }
        this.updateAfterFixedInterval=updateAfterFixedInterval;
    }




    /**
     * Create the location request
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(4000);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);// set displacement in meters ,user must move this distance to get location update
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //initialize the builder and add location request paramenter like HIGH Aurracy
        startLocationUpdates();
        // show the builder to enable location
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        // set builder to always true (Shows the dialog after never operation too)
        builder.setAlwaysShow(true);
        // Then check whether current location settings are satisfied:
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        callbacksResults();
    }


    /**
     * callbacks result on location settings dialog
     */
    public void callbacksResults() {
        // call backs for lcoation status
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            status.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    /**
     * request location update
     */
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (SecurityException e){

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("On Connected");
        createLocationRequest();
        initWebsocket();

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    private void initWebsocket() {
       /* client = new okhttp3.OkHttpClient();*/
        client = new OkHttpClient().newBuilder().pingInterval(30, TimeUnit.SECONDS).build();
        //Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        //TODO add the IP and port
        okhttp3.Request request= new okhttp3.Request.Builder().url(ApiConfig.URL_SOCKET).build();
        listener = new EchoWebSocketListener();
        webSocket= client.newWebSocket(request, listener);

    }

    private void updateOnSocket(Location location){
        Log.e("update on socket..","####");
        if(location!=null) {
            AppPreference appPreference=AppPreference.newInstance(mContext);
            User user=appPreference.getUserDetails();
            if(user.getVehicle_details()!=null) {
                try {
                    String latitude = String.valueOf(location.getLatitude());
                    String longitude = String.valueOf(location.getLongitude());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("lat", latitude);
                    jsonObject.put("driver_id", "" + appPreference.getUserDetails().getId());
                    jsonObject.put("long", longitude);
                    Log.e("driver status.....", AppPreference.newInstance(mContext).getDriverStatus());
                    jsonObject.put("driver_status",AppPreference.newInstance(mContext).getDriverStatus());
                    // check if service is already running
                    if(AppPreference.newInstance(mContext).getStartServiceId()!=0){
                        jsonObject.put("current_running_service", AppPreference.newInstance(mContext).getStartServiceId());
                    }

                    if(user.getVehicle_details()!=null) {
                        int vechile_id=user.getVehicle_details().getVehicle_type_id();
                        if(vechile_id!=0) {
                            jsonObject.put("vehicle_type_id", String.valueOf(vechile_id));
                            Log.e("updateOnSocket####",jsonObject.toString());
                            webSocket.send(jsonObject.toString());
                            UpdateCurentLocation.startLocationUpdate(mContext, appPreference.getUserDetails().getToken(), latitude, longitude);
/*
                            if (updateCount == 50 && isUpdateAfterFixedInterval()) {
                                UpdateCurentLocation.startLocationUpdate(mContext, appPreference.getUserDetails().getToken(), latitude, longitude);
                                updateCount = 0;
                            } else {
                                updateCount++;
                            }*/
                        }
                    }
                } catch (JSONException e) {
                }catch (Exception e){

                }

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Location", "onLocationChanged");
        try {
            newLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateOnSocket(newLocation);
            if (mLocationListener != null) {
                mLocationListener.onLocation(location);
            }
        }catch (SecurityException e){

        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        stopLocationUpdates();
        if (mLocationListener != null) {
            mLocationListener.onLocationFailed();
        }
    }

    /**
     * Stop receiving location updates
     */
    public void stop(){
        if(client!=null && webSocket!=null){
           sendDeaultHeartBeatToSocket();
        }else{
            initWebsocket();
            sendDeaultHeartBeatToSocket();
         }
        if(mLocationRequest!=null){
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        if(mGoogleApiClient!=null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient = null;

        }disconnectSocket();

    }
    private void disconnectSocket(){
        if(webSocket!=null){
            webSocket.close(EchoWebSocketListener.NORMAL_CLOSURE_STATUS, "Goodbye!");
            webSocket=null;
        }if(client!=null){
            // Trigger shutdown of the dispatcher's executor so this process can
            // exit cleanly.
            client.dispatcher().executorService().shutdown();
        }if(listener!=null){
            listener=null;
        }

    }

    public boolean isUpdateAfterFixedInterval() {
        return updateAfterFixedInterval;
    }



    private void sendDeaultHeartBeatToSocket(){
        AppPreference appPreference=AppPreference.newInstance(mContext);
        User user=appPreference.getUserDetails();
        for(int i=0;i<DEFAULT_HEARBEATCOUNT;i++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", "0.0");
                jsonObject.put("driver_id", "" +appPreference.getUserDetails().getId());
                jsonObject.put("long", "0.0");
                if(user.getVehicle_details()!=null && user.getVehicle_details().getVehicle_type_id()!=0) {
                    jsonObject.put("vehicle_type_id", String.valueOf(user.getVehicle_details().getVehicle_type_id()));
                    webSocket.send(jsonObject.toString());
                }

            }catch (JSONException e){
                Log.e(TAG,e.toString());
            }

        }

    }

    public final class EchoWebSocketListener extends WebSocketListener {
        public static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {


        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Receiving: " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            System.out.println("Closing: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
        }

    }
}
