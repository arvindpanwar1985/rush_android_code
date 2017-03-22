package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.utils.AppPreference;


public class SetDriverStatus extends IntentService {

    private static final String ACTION_FOO = "com.hoffmans.rush.services.action.FOO";

    private static final String EXTRA_STATUS = "service_status";
    private static final String EXTRA_ID = "service_id";
    public SetDriverStatus() {
        super("SetStatusService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void updateDriverStatus(Context context, String serviceStatus) {
        Intent intent = new Intent(context, SetDriverStatus.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_STATUS, serviceStatus);


        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String status = intent.getStringExtra(EXTRA_STATUS);
                handleActionStatus(status);
            }
        }
    }

    /**
     * set the service status
     *
     * @param status accepted/running/completed;
     */

    private void handleActionStatus(String status) {
        String token= AppPreference.newInstance(this).getUserDetails().getToken();
        UserRequest userRequest=new UserRequest();
        userRequest.updateDriverStatus(token, status, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {

            }

            @Override
            public void onRequestFailed(String message) {

            }
        });
        }




}
