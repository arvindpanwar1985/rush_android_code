package com.hoffmans.rush.ui.activities;

import android.content.Intent;
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
import com.hoffmans.rush.ui.fragments.UpdateAccountFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Utils;

/**
 * Created by devesh on 19/12/16.
 */

public class LoginActivity extends BaseActivity implements PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    private BrainTreeHandler brainTreeHandler;

    public  static final int IMAGE_REQUEST_PERMISSION=100;
    public  static final int CAMERA_PIC_REQUEST    = 101;
    public  static final int GALLERY_PIC_REQUEST   = 102;
    public  static final int REQUEST_GOOGLE_SIGNIN = 8;
    public  static final String STATUS_PENDING ="pending";
    public  static final String STATUS_ACTIVE ="active";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_login, getParentView());
        //initToolBar(" ",true);
        hideToolbar();
        Fragment fragment=new LoginFragment();
        replaceFragment(fragment,R.id.content_login);
        if(getIntent().getBooleanExtra(Constants.KEY_AUTH_ERROR,false)){
            Utils.showAlertDialog(getApplicationContext(),"Authentication error,Please login Again.");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void setBrainTreeHandler(BrainTreeHandler handler){
        this.brainTreeHandler=handler;
    }

}
