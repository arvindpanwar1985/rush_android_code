package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.ForgotPassFragment;

/**
 * Created by devesh on 20/12/16.
 */

public class ForgotPassActivity extends BaseActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_forgot_pass, getParentView());
        //initToolBar(" ",true);
        hideToolbar();
        Fragment fragment=new ForgotPassFragment();
        replaceFragment(fragment,R.id.content_forgotPass);
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
