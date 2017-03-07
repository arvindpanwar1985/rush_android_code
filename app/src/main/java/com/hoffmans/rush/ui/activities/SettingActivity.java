package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 6/3/17.
 */

public class SettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(getString(R.string.str_settings),true);
        getLayoutInflater().inflate(R.layout.activity_setting,getParentView());
        initViews();
        initListeners();
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
