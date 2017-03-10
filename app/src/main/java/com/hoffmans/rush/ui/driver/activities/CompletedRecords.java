package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.CompletedRecordFragment;


public class CompletedRecords extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_driver_completed_records,getParentView());
        CompletedRecordFragment completedRecordFragment=CompletedRecordFragment.newInstance("","");
        replaceFragment(completedRecordFragment,R.id.contentDriverRecords,true);
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
