package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 7/3/17.
 */

public class LocationRequest extends BaseRequest {


    public void updateUserLocation(String token, String latitude,String longitude,final ApiCallback callback){
        Call<ResponseBody> locationCall=getAPIClient().updateDriverLocation(token,latitude,longitude);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(locationCall);
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
                    if (status){
                        MessageBean bean=new MessageBean();
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
                    }else {
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
