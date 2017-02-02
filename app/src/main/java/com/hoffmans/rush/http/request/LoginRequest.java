package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 23/12/16.
 */

public class LoginRequest extends BaseRequest {

    public void loginUser(String username,String password, final ApiCallback callback){
        Call<ResponseBody> loginCall=getAPIClient().login(username,password);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(loginCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message=obj.getString(MESSAGE);
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        UserBean bean = getGsonBuilder().fromJson(data, UserBean.class);
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
     * @param socialId
     * @param first_name
     * @param last_name
     * @param email
     * @param provider google/facebook
     * @param callback
     * @param pictureUrl
     */
    public void loginViaSocialNetwork(String socialId, String first_name,  String last_name,  String email,  String provider,String pictureUrl,String uuid,String type,String timeZone,final ApiCallback callback){

        Call<ResponseBody> loginCall=getAPIClient().loginViaSocialNetwork(socialId,first_name,last_name,email,provider,pictureUrl,uuid,type,timeZone);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(loginCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message=obj.getString(MESSAGE);
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        UserBean bean = getGsonBuilder().fromJson(data, UserBean.class);
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
