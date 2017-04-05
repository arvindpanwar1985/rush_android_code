package com.hoffmans.rush.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hoffmans.rush.model.User;


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

    /**
     * save complete details of user
     * @param user
     * @throws NullPointerException
     */
    public void saveUser(User user) throws NullPointerException{
        mEditor.putString(PrefConstant.KEY_EMAIL,user.getEmail());
        mEditor.putString(PrefConstant.KEY_FNAME,user.getName());
        mEditor.putString(PrefConstant.KEY_PHONE,user.getPhone());
        mEditor.putInt(PrefConstant.KEY_CURRENCY_ID, user.getCurrency_symbol_id());
        mEditor.putString(PrefConstant.KEY_bt_token,user.getBt_token());
        mEditor.putString(PrefConstant.KEY_ROLE,user.getRole());
        if(user.getPic_url()!=null){
            mEditor.putString(PrefConstant.KEY_PIC,user.getPic_url());
        }
        if(user.getId()!=null){
            mEditor.putInt(PrefConstant.KEY_ID,user.getId());
        }
        mEditor.putString(PrefConstant.KEY_token,user.getToken());
        if(null!=user.getProvider()){
            mEditor.putBoolean(PrefConstant.IS_SOCIAL_PROVIDER,true);
        }
        mEditor.commit();
    }

    /**
     *
     * @return logged in user
     */
    public User getUserDetails(){
        User user =new User();
        user.setEmail(mPref.getString(PrefConstant.KEY_EMAIL,""));
        user.setName(mPref.getString(PrefConstant.KEY_FNAME,""));
        user.setPhone(mPref.getString(PrefConstant.KEY_PHONE,""));
        user.setToken(mPref.getString(PrefConstant.KEY_token,""));
        user.setPic_url(mPref.getString(PrefConstant.KEY_PIC,""));
        user.setBt_token(mPref.getString(PrefConstant.KEY_bt_token,""));
        user.setCurrency_symbol_id(mPref.getInt(PrefConstant.KEY_CURRENCY_ID,0));
        user.setSocialProvider(mPref.getBoolean(PrefConstant.IS_SOCIAL_PROVIDER,false));
        user.setRole(mPref.getString(PrefConstant.KEY_ROLE,""));
        user.setId(mPref.getInt(PrefConstant.KEY_ID,0));
        return  user;

    }

    /**
     * update user when profile changes
     * @param user
     * @throws NullPointerException
     */
    public void updateUserProfile(User user) throws NullPointerException{

        mEditor.putString(PrefConstant.KEY_FNAME,user.getName());
        mEditor.putString(PrefConstant.KEY_PHONE,user.getPhone());
        mEditor.putInt(PrefConstant.KEY_CURRENCY_ID, user.getCurrency_symbol_id());
        if(user.getPic_url()!=null){
            mEditor.putString(PrefConstant.KEY_PIC,user.getPic_url());
        }
        /*if(null!=user.getProvider()){
            mEditor.putBoolean(PrefConstant.IS_SOCIAL_PROVIDER,true);
        }*/

        mEditor.commit();
    }
    public void setUserLogin(boolean isuserLogin){
        mEditor.putBoolean(PrefConstant.ISUSERLOGIN,isuserLogin).commit();
    }

    public boolean isUserLogin(){
        return mPref.getBoolean(PrefConstant.ISUSERLOGIN,false);
    }


    /**
     * clear the session
     */
    public void logoutUser(){

        mEditor.clear().commit();
    }

    /**
     * set presaved notification token
     * @param token
     */
    public void setNotificationToken(String token){
        mEditor.putString(PrefConstant.KEY_NOTIFICATION_TOKEN,token).commit();
    }

    /**
     *
     * @return notification token
     */
    public String getNoticficationToken(){
        return mPref.getString(PrefConstant.KEY_NOTIFICATION_TOKEN,"");
    }

    public void setPause(boolean isPause){
        mPref.edit().putBoolean(PrefConstant.KEY_PAUSE_REUSME,isPause).commit();
    }

    public  boolean getPause(){
        return mPref.getBoolean(PrefConstant.KEY_PAUSE_REUSME,false);
    }

}
