package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Request related to payment module (add credit card payment module etc)
 * Created by devesh on 30/12/16.
 */

public class PaymentRequest extends BaseRequest {


    /**
     *
     * @param authToken authentication token for header
     * @param payment_method_nonce nonce generated from braintree
     * @param callback success and failure callback
     */
    public void addCard(String authToken,String payment_method_nonce, final ApiCallback callback){
        Call<ResponseBody> loginCall=getAPIClient().addCreditCard(authToken,payment_method_nonce);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(loginCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message=obj.getString(MESSAGE);
                    if (status) {
                        MessageBean messageBean=new MessageBean();
                        messageBean.setMessage(message);

                        callback.onRequestSuccess(messageBean);
                    } else {
                        callback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    callback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                callback.onRequestFailed(message);
            }


        });
    }
}
