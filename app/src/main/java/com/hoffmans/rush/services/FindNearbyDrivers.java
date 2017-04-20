package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.NeaabyDriversBean;
import com.hoffmans.rush.http.request.LocationRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.utils.AppPreference;

import org.greenrobot.eventbus.EventBus;

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
    private static final String EXTRA_VECHILE_ID="vechile_id";
    public FindNearbyDrivers() {
        super("SetStatusService");

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void findDrivers(Context context, double lat,double lng,String radius,String vechile_id) {
        Intent intent = new Intent(context, FindNearbyDrivers.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LNG, lng);
        intent.putExtra(EXTRA_RADUIS, radius);
        intent.putExtra(EXTRA_VECHILE_ID, vechile_id);


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
                final String vechile_id=intent.getStringExtra(EXTRA_VECHILE_ID);
                handleActionStatus(radius,lat,lng,vechile_id);
            }
        }
    }



    private void handleActionStatus(String radius,double lat,double lng,String vechile_id) {
        String token= AppPreference.newInstance(this).getUserDetails().getToken();
        LocationRequest userRequest=new LocationRequest();
        userRequest.getNearbyDrivers(token, String.valueOf(lat),String.valueOf(lng),radius, vechile_id,new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {

                if(body!=null){
                    NeaabyDriversBean neaabyDriversBean=(NeaabyDriversBean)body;
                    if(neaabyDriversBean!=null){
                        neaabyDriversBean.setFoundDrivers(true);
                        postEvent(neaabyDriversBean);
                    }else{
                        NeaabyDriversBean neaabyDriversBean1=new NeaabyDriversBean();
                        neaabyDriversBean1.setFoundDrivers(false);
                        postEvent(neaabyDriversBean1);
                    }
                }
            }

            @Override
            public void onRequestFailed(String message) {
                NeaabyDriversBean neaabyDriversBean1=new NeaabyDriversBean();
                neaabyDriversBean1.setFoundDrivers(false);
                postEvent(neaabyDriversBean1);
            }
        });
    }

    private void postEvent(NeaabyDriversBean neaabyDriversBean){
        EventBus.getDefault().post(neaabyDriversBean);
    }



}
