package com.hoffmans.rush.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

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

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.hoffmans.rush.location.LocationData.REQUEST_CHECK_SETTINGS;
import static com.hoffmans.rush.location.LocationUpdate.EchoWebSocketListener.NORMAL_CLOSURE_STATUS;


/**
 * Created by devesh on 11/12/15.
 */
public class LocationUpdate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final long INTERVAL      =15000;
    private static final int DISPLACEMENT   =25;
    private static LocationUpdate mLocationUpdate =null;
    private LocationInterface mLocationListener =null;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;
    private EchoWebSocketListener listener;
    private  WebSocket webSocket;
    private okhttp3.OkHttpClient client;
    private Location newLocation;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private LocationUpdate(){

    }
    public static LocationUpdate getInstance(){
        if(mLocationUpdate ==null){
            mLocationUpdate =new LocationUpdate();
        }
        return mLocationUpdate;
    }
    public void startLocationUpdate(Context context, LocationInterface locationInterface){
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

    }


    /**
     * Create the location request
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
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
        client = new okhttp3.OkHttpClient();
        //Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        //TODO add the IP and port
        okhttp3.Request request= new okhttp3.Request.Builder().url("ws://echo.websocket.org").build();
        listener = new EchoWebSocketListener();
        webSocket= client.newWebSocket(request, listener);
    }
    private void updateOnSocket(Location location){
        if(location!=null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", "" + location.getLatitude());
                jsonObject.put("lng", "" + location.getLongitude());
                webSocket.send(jsonObject.toString());
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

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
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
            webSocket=null;
        }if(client!=null){
            // Trigger shutdown of the dispatcher's executor so this process can
            // exit cleanly.
            client.dispatcher().executorService().shutdown();
        }if(listener!=null){
            listener=null;
        }

    }


    public final class EchoWebSocketListener extends WebSocketListener {
        public static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            //webSocket.send("Hello!");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
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
