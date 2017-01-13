package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.PaymentMethodFragment;
import com.hoffmans.rush.ui.fragments.RegisterFragment;
import com.hoffmans.rush.ui.fragments.UpdateAccountFragment;

/**
 * Created by devesh on 19/12/16.
 */

public class CreateAccountActivity extends BaseActivity  {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_create_account, getParentView());
        initToolBar(" ",false);
        hideToolbar();
        Fragment fragment=new RegisterFragment();
        replaceFragment(fragment,R.id.content_create_account,false);
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
        String lastFragmentName= fragmentManager.getBackStackEntryAt(fragmentSize).getName();
        if(lastFragmentName.equals(UpdateAccountFragment.class.getCanonicalName())){
           return;
        }
        if(lastFragmentName.equals(PaymentMethodFragment.class.getCanonicalName())){
            return;
        }
        super.onBackPressed();
    }


}
