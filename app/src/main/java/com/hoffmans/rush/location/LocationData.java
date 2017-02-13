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

//import com.facebook.internal.Utility.Constants;


/**
 * Created by devesh.bhandari on 4/28/2015.
 */
public class LocationData implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Context _context;
    private Location latKnowLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationInterface mlocationInterface;
    public static  final int REQUEST_CHECK_SETTINGS=500;


    private PendingResult<LocationSettingsResult> result;

    //Location requests
    private LocationRequest locationRequest = LocationRequest.create()
            .setInterval(10 * 60 * 1000) // every 10 minutes
            .setExpirationDuration(10 * 1000) // After 10 seconds
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private LocationSettingsRequest.Builder builder;

    public LocationData(Context con,LocationInterface locationInterface) {

        _context = con;
        setonLocationListener(locationInterface);
        mGoogleApiClient = new GoogleApiClient.Builder(con.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        //initialize the builder and add location request paramenter like HIGH Aurracy
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
       // set builder to always true (Shows the dialog after never operation too)
        builder.setAlwaysShow(true);
       // Then check whether current location settings are satisfied:
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        callbacksResults();


       }

    // call back for other class
    public void callbacksResults()
    {
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
                         status.startResolutionForResult((Activity)_context,REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;

                }
            }
        });
    }




    @Override
    public void onConnected(Bundle bundle) {
        try {
            latKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (latKnowLocation != null) {
                mlocationInterface.onLocation(latKnowLocation);
            } else {
                mlocationInterface.onLocationFailed();
            }
        }catch (SecurityException e){

        }
    }

    /**
     * set the location  interface
     * @param locationInterface
     */
    public void setonLocationListener(LocationInterface locationInterface) {
        mlocationInterface = locationInterface;
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        mlocationInterface.onLocationFailed();
    }

    /**
     * disconnect the google APi client
     */
    public  void disconnect() {

        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
    }


    public Location getLatKnowLocation() {
        try {
            latKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
         }catch (SecurityException e){
           return null;
        }
        return latKnowLocation;
    }


}
