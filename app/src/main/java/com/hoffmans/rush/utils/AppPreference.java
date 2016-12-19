package com.hoffmans.rush.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreference {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefUtils;
    private SharedPreferences.Editor mEditorUtils;
    private Context _context;
    private int mPRIVATE_MODE = 0;
    static AppPreference sessionObj;
    private final String mPREF_NAME = "RUSH_PREF";
    private final String mPREF_NAMEUTILS = "RUSH_PREF_UTILS";


    public AppPreference(Context context) {
        this._context = context;
        mPref = _context.getSharedPreferences(mPREF_NAME, mPRIVATE_MODE);
        mEditor = mPref.edit();
        mPrefUtils = _context.getSharedPreferences(mPREF_NAMEUTILS, mPRIVATE_MODE);
        mEditorUtils=mPrefUtils.edit();


    }

    public static AppPreference newInstance(Context context) {
        if (sessionObj == null)
            sessionObj = new AppPreference(context);
        return sessionObj;
    }




}
