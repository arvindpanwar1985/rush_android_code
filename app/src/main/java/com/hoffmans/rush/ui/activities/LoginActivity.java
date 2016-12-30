package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.BrainTreeHandler;
import com.hoffmans.rush.ui.fragments.LoginFragment;
import com.hoffmans.rush.ui.fragments.PaymentMethodFragment;

/**
 * Created by devesh on 19/12/16.
 */

public class LoginActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeErrorListener {


    private BrainTreeHandler brainTreeHandler;
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

    public void setBrainTreeHandler(BrainTreeHandler handler){
        this.brainTreeHandler=handler;
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        int fragmentSize=fragmentManager.getBackStackEntryCount()-1;
        if(!fragmentManager.getBackStackEntryAt(fragmentSize).getName().equals(PaymentMethodFragment.class.getCanonicalName())){
            super.onBackPressed();
        }
    }

    @Override
    public void onError(Exception error) {
        brainTreeHandler.onError(error);
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        brainTreeHandler.onNonceCreated(paymentMethodNonce);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(brainTreeHandler!=null){
            brainTreeHandler=null;
        }
    }
}
