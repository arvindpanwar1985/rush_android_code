package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;

public class AcceptOrderActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar("",false);
        hideToolbar();
        getLayoutInflater().inflate(R.layout.activity_accept_order,getParentView());
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
