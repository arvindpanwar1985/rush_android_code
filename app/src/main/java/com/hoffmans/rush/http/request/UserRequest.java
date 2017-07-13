package com.hoffmans.rush.http.request;

import android.content.Context;

import com.google.gson.JsonObject;
import com.hoffmans.rush.bean.ForgotPassBean;
import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.utils.AppPreference;

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
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status){
                        String data = obj.getJSONObject(DATA).toString();
                        UserBean bean = getGsonBuilder().fromJson(data, UserBean.class);
                        bean.setMessage(message);
                        callback.onRequestSuccess(bean);
                    }else{
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
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
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
     * @param email
     * @param callback
     */

    public void forgotPass(String email,final ApiCallback callback){

        Call<ResponseBody> createUserCall=getAPIClient().forgotPassword(email);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(createUserCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        ForgotPassBean forgotPassBean=new ForgotPassBean();
                        forgotPassBean.setMessage(message);
                        callback.onRequestSuccess(forgotPassBean);
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


    public void updateUserWithImageData(String token,Map<String,RequestBody> requestBodyMap, MultipartBody.Part file,final ApiCallback callback){
        Call<ResponseBody> createUserCall=getAPIClient().updateUserWithImage(token,requestBodyMap, file);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(createUserCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
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


    public void getProfile(String token, final ApiCallback callback){
        Call<ResponseBody> userCall=getAPIClient().getProfile(token);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(userCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
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
     * @param token
     * @param callback
     */
    public void getPendingRequest(String token, final Context context, final ApiCallback callback){
        Call<ResponseBody> userCall=getAPIClient().getPendingRequest(token);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(userCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        JSONObject jsonObject=obj.getJSONObject("data");
                        JSONObject serviceJson=jsonObject.getJSONObject("upcoming_services");
                        String servieId=serviceJson.getString("id");
                        AppPreference.newInstance(context).setServiceId(servieId);

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
     * Update the driver status
     * @param token
     * @param status active/inactive
     * @param callback
     */
    public  void updateDriverStatus(String token,String status,final ApiCallback callback){
        Call<ResponseBody> updateCall=getAPIClient().updateStatus(token,status);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(updateCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
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

    /**
     * get driver details
     * @param auth
     * @param url
     * @param callback
     */
    public void driverShow(String auth,String url, final  ApiCallback callback){
        Call<ResponseBody> showCall=getAPIClient().driverShow(auth,url);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(showCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
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
     * add comments
     * @param auth
     * @param serviceId
     * @param comment
     * @param callback
     */
    public void addComment(String auth,int serviceId,String comment ,final  ApiCallback callback){
        Call<ResponseBody> commentCall=getAPIClient().addDriverComment(auth,serviceId,comment);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(commentCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        MessageBean bean=new MessageBean();
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
     * logut user from app
     * @param auth
     * @param uuid
     * @param callback
     */
    public void userLogout(String auth,String uuid,final ApiCallback callback){
        Call<ResponseBody> logoutCall=getAPIClient().logoutUser(auth,uuid);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(logoutCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    String message,msg1="";
                    boolean status = obj.getBoolean(SUCCESS);
                    String msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)) {
                        message=msg;
                    }else{
                        msg1=obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }

                    if (status) {
                        MessageBean bean=new MessageBean();
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
