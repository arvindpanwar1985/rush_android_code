package com.hoffmans.rush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by devesh on 6/3/17.
 * Broadcast Receiver for locale change
 */

public class LocaleChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.LOCALE_CHANGED")){
           // Toast.makeText(context,"Locale :"+ Locale.getDefault().getLanguage(),Toast.LENGTH_SHORT).show();
        }
    }
}
