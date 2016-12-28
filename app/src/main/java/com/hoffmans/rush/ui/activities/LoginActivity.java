package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.LoginFragment;
import com.hoffmans.rush.ui.fragments.PaymentMethodFragment;

/**
 * Created by devesh on 19/12/16.
 */

public class LoginActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_login, getParentView());
        //initToolBar(" ",true);
        hideToolbar();
        Fragment fragment=new LoginFragment();
        replaceFragment(fragment,R.id.content_login);
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
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        int fragmentSize=fragmentManager.getBackStackEntryCount()-1;
        if(!fragmentManager.getBackStackEntryAt(fragmentSize).getName().equals(PaymentMethodFragment.class.getCanonicalName())){
            super.onBackPressed();
        }
    }
}
