package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.UpcomingFragment;

public class UpcomingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_upcoming,getParentView());
        UpcomingFragment fragment=UpcomingFragment.newInstance();
        replaceFragment(fragment,R.id.contentUpcoming,true);

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
