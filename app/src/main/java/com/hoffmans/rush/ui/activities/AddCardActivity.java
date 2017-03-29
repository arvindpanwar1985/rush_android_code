package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.BrainTreeHandler;
import com.hoffmans.rush.ui.fragments.AddCardFragment;
import com.hoffmans.rush.utils.AppPreference;

public class AddCardActivity extends BaseActivity  implements PaymentMethodNonceCreatedListener, BraintreeErrorListener {

    public static final String KEY_CARD_DATA="key_card_data";
    public  static final  int REQUEST_ADD_CARD=190;
    private BrainTreeHandler brainTreeHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_card, getParentView());
        AppPreference appPreference=AppPreference.newInstance(this);
        AddCardFragment fragment=AddCardFragment.newInstance(appPreference.getUserDetails().getBt_token());
        replaceFragment(fragment,R.id.content_add_card,false);
    }

    @Override
    protected void initViews() {
      // init view here
    }

    @Override
    protected void initListeners() {
      //init listener here
    }

    @Override
    protected void initManagers() {
       //init manager
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * set brain-tree handler
     * @param handler
     */
    public void setBrainTreeHandler(BrainTreeHandler handler){
        this.brainTreeHandler=handler;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(brainTreeHandler!=null){
            brainTreeHandler=null;
        }
    }
}
