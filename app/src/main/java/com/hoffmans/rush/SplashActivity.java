package com.hoffmans.rush;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.activities.LoginActivity;
import com.hoffmans.rush.ui.driver.fragments.LoginFragment;
import com.hoffmans.rush.utils.AppPreference;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private static final int SPLASH_TIMER = 2000;
    private AppPreference appPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        appPreference=AppPreference.newInstance(this);

        initHandler();
    }
    /*
    * initialize Runnable and Handler to proceed the splash activity functionality.
    */
    private void initHandler() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(appPreference.isUserLogin() && appPreference.getUserDetails().getRole().equals(LoginFragment.ROLE_CUST)){
                    Intent bookServiceIntent=new Intent(SplashActivity.this, BookServiceActivity.class);
                    bookServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(bookServiceIntent);
                }else {
                    callActivity();
                }
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, SPLASH_TIMER);
    }


    /**
     * call activity from handler
     */
    private void callActivity() {
        Intent loginIntent=new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        this.finish();
    }

}
