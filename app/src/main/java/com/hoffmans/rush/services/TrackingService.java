package com.hoffmans.rush.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.location.LocationUpdate;



public class TrackingService extends Service implements LocationInterface {
    private LocationUpdate locationUpdate = null;

    public TrackingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationUpdate = LocationUpdate.getInstance();
        locationUpdate.startLocationUpdate(this, this,true);


   }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(locationUpdate==null){
            locationUpdate = LocationUpdate.getInstance();
            locationUpdate.startLocationUpdate(this, this,true);
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop location update and stop updating socket
        locationUpdate.stop();
    }
    @Override
    public void onLocation(Location location) {
       // Log.v("location",location.getLatitude()+":"+location.getLongitude());
    }
    @Override
    public void onLocationFailed() {
       Log.v("location","failed");
    }


}
