package com.hoffmans.rush.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;



public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
