package com.hoffmans.rush.listners;

import com.braintreepayments.api.models.PaymentMethodNonce;

/**
 * Created by devesh on 29/12/16.
 */

public interface BrainTreeHandler {

    void onError(Exception error);
    void onNonceCreated(PaymentMethodNonce paymentMethodNonce);
}
