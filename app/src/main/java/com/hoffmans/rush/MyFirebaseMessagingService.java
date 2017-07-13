package com.hoffmans.rush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoffmans.rush.ui.driver.activities.AcceptOrderActivity;
import com.hoffmans.rush.ui.driver.activities.RatingActivity;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
/**
 * Created by devesh on 29/12/16.
 *
 * Class to receive app notifications
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String ROLE_DRIVER            ="Driver";
    private static final String ROLE_CUSTOMER          ="Customer";
    private static final String TYPE_ACCEPT_ORDER      ="driver_assigned";
    private static final String TYPE_DRIVER_ASSIGNED   ="driver_assigned";
    private static final String TYPE_SERVICE_COMPLETED ="service_completed";
    private static final String TYPE_SERVICE_ACCEPTED  ="service_accepted";
    private static final String TYPE_SERVICE_RUNNING="service_running";
    private static final String TYPE_DRIVER_MESSAGE="message_notification";
    private  String KEY_NOTIFY_TYPE  ="type";
    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";
    private AppPreference mAppPreference;
    private static final String TAG = MyFirebaseMessagingService.class.getCanonicalName();
    private Intent acceptOrderIntent;
    private String notifyType;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mAppPreference=AppPreference.newInstance(this);
        Map<String, String> params = remoteMessage.getData();
        if(remoteMessage.getData().size()>0) {
            JSONObject object = new JSONObject(params);
            Log.e("object#######",object.toString());

            String role = mAppPreference.getUserDetails().getRole();
            if (!TextUtils.isEmpty(role) && role != null) {
                switch (role) {
                    case ROLE_DRIVER:
                        if(object.has(KEY_NOTIFY_TYPE)) {
                            try {
                                String notificationType = object.getString(KEY_NOTIFY_TYPE);
                                notifyType=notificationType;
                                handleDriverNotifications(object,notificationType);
                            }catch (JSONException e){

                            }
                        }
                        break;
                    case ROLE_CUSTOMER:
                        if(object.has(KEY_NOTIFY_TYPE)) {
                            try {
                                String notificationType = object.getString(KEY_NOTIFY_TYPE);
                                notifyType=notificationType;
                                handleCustomerNotifications(object,notificationType);
                            }catch (JSONException e){

                            }
                        }
                        break;
                }
            }
        }

    }

    /**
     * handle notification related to driver
     * @param object object from notification
     * @param notifyType type of notification
     */
    private void handleDriverNotifications(JSONObject object,String  notifyType){
        try{
            switch (notifyType){
                case TYPE_ACCEPT_ORDER:
                    String message   =object.getString(KEY_MESSAGE);
                    String serviceID =object.getString(KEY_SERVICE_ID);


                    if(!mAppPreference.getPause()) {
                        EventBus.getDefault().postSticky(serviceID);
                    }else{

                        if(Utils.isAppIsInBackground(getApplicationContext())){

                            sendNotification(message,TYPE_ACCEPT_ORDER,serviceID);
                            //showNotification(message,acceptOrderIntent);
                        }else{

                           // AppPreference.newInstance(this).setServiceId(serviceID);
                            // start activity to accept the order
                            acceptOrderIntent=new Intent(getApplicationContext(), AcceptOrderActivity.class);
                            acceptOrderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            acceptOrderIntent.putExtra(KEY_MESSAGE,message);
                            acceptOrderIntent.putExtra(KEY_SERVICE_ID,serviceID);
                            // start activity to accept the order
                            startActivity(acceptOrderIntent);
                        }


                    }

                    break;

                case TYPE_DRIVER_MESSAGE:
                    String message1   =object.getString(KEY_MESSAGE);

                    AppPreference.newInstance(getApplicationContext()).setAdminMessage(message1);

                    sendNotification(message1,TYPE_DRIVER_MESSAGE,"");
                    break;

            }
        }catch (JSONException e){
            Log.i(TAG,e.toString());
        }
    }


    /**
     *handle notification related to customer
     * @param object object from notification
     * @param notifyType type of notification
     */
    private void handleCustomerNotifications(JSONObject object,String  notifyType){
        try{

            String message   =object.getString(KEY_MESSAGE);
            String serviceID =object.getString(KEY_SERVICE_ID);
            switch (notifyType){

                case TYPE_SERVICE_COMPLETED:


                    if(Utils.isAppIsInBackground(this)){

                       /* Intent ratingIntent=new Intent(this, RatingActivity.class);
                        ratingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ratingIntent.putExtra(KEY_MESSAGE,message);
                        ratingIntent.putExtra(KEY_SERVICE_ID,serviceID);*/
                        //startActivity(ratingIntent);
                        sendNotification(message,TYPE_SERVICE_COMPLETED,serviceID);

                    }else{
                       // sendNotification(message,"",serviceID);
                        Intent ratingIntent=new Intent(this, RatingActivity.class);
                        ratingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ratingIntent.putExtra(KEY_MESSAGE,message);
                        ratingIntent.putExtra(KEY_SERVICE_ID,serviceID);
                        startActivity(ratingIntent);
                    }
                    break;

                case TYPE_DRIVER_ASSIGNED:

                    if(Utils.isAppIsInBackground(this)){
                        sendNotification(message,"",serviceID);
                    }else{
                        sendNotification(message,"",serviceID);
                    }

                    break;
                case TYPE_SERVICE_ACCEPTED:
                    if(Utils.isAppIsInBackground(this)){
                        sendNotification(message,TYPE_SERVICE_ACCEPTED,serviceID);

                    }else{
                        sendNotification(message,"",serviceID);
                    }

                case TYPE_SERVICE_RUNNING:
                    if(Utils.isAppIsInBackground(this)){
                        sendNotification(message,TYPE_SERVICE_RUNNING,serviceID);

                    }else{
                        sendNotification(message,"",serviceID);
                    }
                    break;
            }
        }catch (JSONException e){
            Log.i(TAG,e.toString());
        }
    }




    /**
     * show the notification icon
     * @param message messgae to show in notification
     */
    private void sendNotification(String message,String type,String serviceID){
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH);

       //No intent after notification clicked
        //PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        if(type.equals(TYPE_SERVICE_COMPLETED)){
            Intent ratingIntent=new Intent(this, RatingActivity.class);
            ratingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ratingIntent.putExtra(KEY_MESSAGE,message);
            ratingIntent.putExtra(KEY_SERVICE_ID,serviceID);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ratingIntent, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pendingIntent);

        } else if(!type.equals("")){
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(),SplashActivity.class), PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pendingIntent);
        }

        /*else if(type.equals(TYPE_ACCEPT_ORDER) ){
            // start activity to accept the order
            *//*acceptOrderIntent = new Intent(getApplicationContext(), AcceptOrderActivity.class);
            acceptOrderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            acceptOrderIntent.putExtra(KEY_MESSAGE, message);
            acceptOrderIntent.putExtra(KEY_SERVICE_ID, serviceID);*//*
            //startActivity(acceptOrderIntent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,  new Intent(getApplicationContext(),SplashActivity.class), PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pendingIntent);

        }*/

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}