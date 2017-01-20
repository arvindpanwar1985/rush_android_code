package com.hoffmans.rush;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hoffmans.rush.utils.AppPreference;

/**
 * Created by devesh on 29/12/16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = MyFirebaseInstanceIDService.class.getCanonicalName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        AppPreference appPreference=AppPreference.newInstance(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // save new token to preferences
        appPreference.setNotificationToken(refreshedToken);
    }
}
