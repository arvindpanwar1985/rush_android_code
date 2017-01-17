package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.ConfirmServiceFragment;

/**
 * Created by devesh on 11/1/17.
 */

public class ConfirmServiceActivity extends BaseActivity {

    public static final String KEY_CARD_DATA="key_card_data";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_confirm_service, getParentView());
        initToolBar(" ",true);
        ConfirmServiceFragment fragment=new ConfirmServiceFragment();
        replaceFragment(fragment,R.id.content_confirm_servicer,false);



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
