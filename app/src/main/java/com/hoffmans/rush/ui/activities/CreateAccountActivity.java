package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.BrainTreeHandler;
import com.hoffmans.rush.ui.fragments.PaymentMethodFragment;
import com.hoffmans.rush.ui.fragments.RegisterFragment;
import com.hoffmans.rush.ui.fragments.UpdateAccountFragment;

/**
 * Created by devesh on 19/12/16.
 */

public class CreateAccountActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeErrorListener {


    private BrainTreeHandler brainTreeHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_create_account, getParentView());
        initToolBar(" ",false);
        //hideToolbar();
        Fragment fragment=new RegisterFragment();
        replaceFragment(fragment,R.id.content_create_account);
    }



    public void setBrainTreeHandler(BrainTreeHandler handler){
        this.brainTreeHandler=handler;
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
