package com.hoffmans.rush.http.request;


import android.util.Log;

import com.google.gson.JsonObject;
import com.hoffmans.rush.bean.CardListBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;



/**
 * Request related to payment module (add credit card payment module etc)
 * Created by devesh on 30/12/16.
 */

public class PaymentRequest extends BaseRequest {



    /**
     *
     * @param authToken
     * @param callback
     */
    public void addPayPalCard(String authToken, JsonObject jsonObject, final ApiCallback callback){

        HashMap<String,Object> objectHashMap=new HashMap<>();
        objectHashMap.put("creditcard",jsonObject);

        Call<ResponseBody> loginCall=getAPIClient().addPayPalCreditCard(authToken, objectHashMap);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(loginCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message="",msg1="";
                    if(obj.has(MESSAGE)){
                        String msg=obj.getString(MESSAGE);
                        if(!obj.has(SPANISH_MESSAGE)) {
                            message=msg;
                        }else{
                            msg1=obj.getString(SPANISH_MESSAGE);
                            message=parseMessageUsingLocale(msg,msg1);
                        }
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        CardListBean bean = getGsonBuilder().fromJson(data, CardListBean.class);
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
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






    /**
     *
     * @param authToken
     * @param callback
     */
    public void getPayPalPaymentList(String authToken,final ApiCallback callback){

        Call<ResponseBody> listCardCall=getAPIClient().getPayPalCardList(authToken);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(listCardCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {

                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message="",msg1="";
                    if(obj.has(MESSAGE)){
                        String msg=obj.getString(MESSAGE);
                        if(!obj.has(SPANISH_MESSAGE)) {
                            message=msg;
                        }else{
                            msg1=obj.getString(SPANISH_MESSAGE);
                            message=parseMessageUsingLocale(msg,msg1);
                        }
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        CardListBean bean = getGsonBuilder().fromJson(data, CardListBean.class);
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
                    } else {
                        Log.e("onWebServiceComplete...",message);
                        callback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    Log.e("onRequestFailed...",e.getMessage());
                    callback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                Log.e("onWebStatusFalse...",message);
                callback.onRequestFailed(message);
            }


        });
    }




    /**
     *
     * @param header
     * @param callback
     */
    public void deletePayPalCard(String header, HashMap<String,String> params,final ApiCallback callback){
        Call<ResponseBody> deleteCardCall=getAPIClient().deletePayPalCard(header, params);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(deleteCardCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {

                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message="",msg1="";
                    if(obj.has(MESSAGE)){
                        String msg=obj.getString(MESSAGE);
                        if(!obj.has(SPANISH_MESSAGE)) {
                            message=msg;
                        }else{
                            msg1=obj.getString(SPANISH_MESSAGE);
                            message=parseMessageUsingLocale(msg,msg1);
                        }
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        CardListBean bean = getGsonBuilder().fromJson(data, CardListBean.class);
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
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




    public void defaultPayPalCard(String header, String cardId,final ApiCallback callback){
        Call<ResponseBody> deleteCardCall=getAPIClient().defaultPayPalCard(header, cardId);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(deleteCardCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    String message="";
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    if(obj.has(MESSAGE)){
                        String msg=obj.getString(MESSAGE);
                        String msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        CardListBean bean = getGsonBuilder().fromJson(data, CardListBean.class);
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
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
