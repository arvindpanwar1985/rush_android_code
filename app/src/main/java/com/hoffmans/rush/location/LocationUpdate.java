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

import static com.hoffmans.rush.location.LocationData.REQUEST_CHECK_SETTINGS;


/**
 * Created by devesh on 11/12/15.
 */
public class LocationUpdate implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static LocationUpdate mLocationUpdate =null;
    private LocationInterface mLocationListener =null;
    private String mUrl;
    private int mPort;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;


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

    public void startLocationUpdate(Context context, LocationInterface locationInterface, String url, int port){
        // Create an instance of GoogleAPIClient.
        this.mContext=context;
        this.mUrl=url;
        this.mPort=port;
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
        mLocationRequest.setInterval(15000);
        mLocationRequest.setSmallestDisplacement(25);// set displacement in meters ,user must move this distance to get location update
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        startLocationUpdates();
        //initialize the builder and add location request paramenter like HIGH Aurracy
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        // set builder to always true (Shows the dialog after never operation too)
        builder.setAlwaysShow(true);
        // Then check whether current location settings are satisfied:
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        callbacksResults();

    }


    // call back for other class
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
        initializeSocketIO(mUrl, mPort);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            Location latKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateOnSocket(latKnowLocation);
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

    private void initializeSocketIO(final String addr, final int port) {


    }
    private void updateOnSocket(Location location){


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

        }
         //TODO uncomment while implementing
        /*if(socketio!=null){
            socketio.disconnect();
            socketio=null;
        }*/
    }



}
