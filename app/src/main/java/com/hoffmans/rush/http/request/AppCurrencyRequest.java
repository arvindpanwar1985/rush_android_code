package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.CurrencyBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 6/1/17.
 */

public class AppCurrencyRequest extends BaseRequest {
    /**
     * @param callback
     */
    public void getCurrency(final ApiCallback callback){
        Call<ResponseBody> currencyCall=getAPIClient().getCurrency();
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(currencyCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody){
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
                        CurrencyBean bean = getGsonBuilder().fromJson(data, CurrencyBean.class);
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
