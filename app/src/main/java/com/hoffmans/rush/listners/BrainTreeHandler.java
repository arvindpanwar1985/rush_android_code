package com.hoffmans.rush.listners;

import com.braintreepayments.api.models.PaymentMethodNonce;

/**
 * Created by devesh on 29/12/16.
 */

public interface BrainTreeHandler {

    public void onError(Exception error);
    public void onNonceCreated(PaymentMethodNonce paymentMethodNonce);
}
