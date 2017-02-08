package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.LoginFragment;

/**
 * Created by devesh on 8/2/17.
 */

public class DriverLoginActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_driver_login, getParentView());
        LoginFragment fragment=LoginFragment.newInstance();
        replaceFragment(fragment,R.id.content_driver_login,true);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initManagers() {

    }
}
