package com.hoffmans.rush.http.request;

import com.google.gson.JsonObject;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 23/12/16.
 */

public class UserRequest extends BaseRequest {


    /**
     *
     * @param requestBodyMap request body map
     * @param file          Multipart file
     * @param callback
     */
    public void createUser(Map<String,RequestBody> requestBodyMap, MultipartBody.Part file,final ApiCallback callback){


        Call<ResponseBody> createUserCall=getAPIClient().createUser(requestBodyMap, file);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(createUserCall);
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
     * @param email email of user
     * @param phone phone number
     * @param callback
     */

    public void updateUser(JsonObject object,String token, final ApiCallback callback){

        HashMap<String,Object> objectHashMap=new HashMap<>();
        objectHashMap.put("user",object);
        Call<ResponseBody> createUserCall=getAPIClient().updateUser(token,objectHashMap);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(createUserCall);
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
