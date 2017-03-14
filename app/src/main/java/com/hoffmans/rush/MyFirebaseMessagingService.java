package com.hoffmans.rush;

import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoffmans.rush.ui.driver.activities.AcceptOrderActivity;
import com.hoffmans.rush.utils.AppPreference;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by devesh on 29/12/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String ROLE_DRIVER       ="driver";
    private static final String ROLE_CUSTOMER     ="customer";
    private static final String TYPE_ACCEPT_ORDER ="assign_driver";


    private  String KEY_NOTIFY_TYPE  ="type";
    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";
    private AppPreference mAppPreference;
    private static final String TAG = MyFirebaseMessagingService.class.getCanonicalName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        mAppPreference=AppPreference.newInstance(this);
        Map<String, String> params = remoteMessage.getData();
        if(remoteMessage.getData().size()>0) {
            JSONObject object = new JSONObject(params);
            String role = mAppPreference.getUserDetails().getRole();
            if (!TextUtils.isEmpty(role) && role != null) {
                switch (role) {
                    case ROLE_DRIVER:
                        if(object.has(KEY_NOTIFY_TYPE)) {
                            try {
                                String notificationType = object.getString(KEY_NOTIFY_TYPE);
                                handleDriverNotifications(object,notificationType);
                            }catch (JSONException e){

                            }
                        }
                        break;
                    case ROLE_CUSTOMER:
                        if(object.has(KEY_NOTIFY_TYPE)) {
                            try {
                                String notificationType = object.getString(KEY_NOTIFY_TYPE);
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

                        Intent acceptOrderIntent=new Intent(this, AcceptOrderActivity.class);
                        acceptOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        acceptOrderIntent.putExtra(KEY_MESSAGE,message);
                        acceptOrderIntent.putExtra(KEY_SERVICE_ID,serviceID);
                        startActivity(acceptOrderIntent);
                    }

                    break;
            }
        }catch (Exception e){

        }
    }


    /**
     *handle notification related to customer
     * @param object object from notification
     * @param notifyType type of notification
     */
    private void handleCustomerNotifications(JSONObject object,String  notifyType){
        try{

        }catch (Exception e){

        }
    }
}