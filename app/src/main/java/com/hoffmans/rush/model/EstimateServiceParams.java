package com.hoffmans.rush.model;

/**
 * Created by devesh on 16/1/17.
 */

public class EstimateServiceParams {

    private String payment_method_token;
    private int transaction_id;
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getPayment_method_token() {
        return payment_method_token;
    }

    public void setPayment_method_token(String payment_method_token) {
        this.payment_method_token = payment_method_token;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }
}
