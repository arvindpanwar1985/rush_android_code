package com.hoffmans.rush;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by devesh on 29/12/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getCanonicalName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
       // Map<String ,String> hashMap=remoteMessage.getData();
      //  Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}