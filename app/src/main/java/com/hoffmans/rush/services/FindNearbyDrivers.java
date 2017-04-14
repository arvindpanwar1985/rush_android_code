package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.NeaabyDriversBean;
import com.hoffmans.rush.http.request.LocationRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.utils.AppPreference;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FindNearbyDrivers extends IntentService {

    private static final String ACTION_FOO = "com.hoffmans.rush.services.action.FOO";

    private static final String EXTRA_LAT = "lat";
    private static final String EXTRA_LNG= "lng";
    private static final String EXTRA_RADUIS="radius";
    public FindNearbyDrivers() {
        super("SetStatusService");

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void findDrivers(Context context, double lat,double lng,String radius) {
        Intent intent = new Intent(context, FindNearbyDrivers.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LNG, lng);
        intent.putExtra(EXTRA_RADUIS, radius);


        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String radius = intent.getStringExtra(EXTRA_RADUIS);
                final double lat=intent.getDoubleExtra(EXTRA_LAT,0.0);
                final double lng=intent.getDoubleExtra(EXTRA_LNG,0.0);
                handleActionStatus(radius,lat,lng);
            }
        }
    }



    private void handleActionStatus(String radius,double lat,double lng) {
        String token= AppPreference.newInstance(this).getUserDetails().getToken();
        LocationRequest userRequest=new LocationRequest();
        userRequest.getNearbyDrivers(token, String.valueOf(lat),String.valueOf(lng),radius, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {

                if(body!=null){
                    NeaabyDriversBean neaabyDriversBean=(NeaabyDriversBean)body;
                    if(neaabyDriversBean.getListNearbyDrivers().size()!=0){
                        List<User> nearbyDrivers=neaabyDriversBean.getListNearbyDrivers();
                        EventBus.getDefault().post(nearbyDrivers);
                    }
                }
            }

            @Override
            public void onRequestFailed(String message) {

            }
        });
    }




}
