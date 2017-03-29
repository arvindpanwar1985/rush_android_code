package com.hoffmans.rush.ui.driver.activities;

import android.content.Intent;
import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.UpcomingFragment;
import com.hoffmans.rush.utils.Status;

public class UpcomingActivity extends BaseActivity {

    private UpcomingFragment fragment;
    public static final String DEFAULT_ITEMS      ="5";
    public static final String DEFAULT_PAGE_NO    ="1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_upcoming,getParentView());
        fragment=UpcomingFragment.newInstance();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //call api for loading upcoming data for driver
        fragment.getScheduledAndCurrentSercices(DEFAULT_PAGE_NO,DEFAULT_ITEMS, Status.ACCEPTED);
    }
}
