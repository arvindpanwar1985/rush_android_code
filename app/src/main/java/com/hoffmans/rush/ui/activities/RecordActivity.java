package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.RecordFragment;

/**
 * Created by devesh on 24/1/17.
 */

public class RecordActivity extends BaseActivity {

    private static final String KEY_IS_RECORD="isRecord";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_record, getParentView());
        boolean isRecord=getIntent().getBooleanExtra(KEY_IS_RECORD,false);
        Fragment recordFragment= RecordFragment.newInstance(isRecord);
        replaceFragment(recordFragment,R.id.contentRecord,true);
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
