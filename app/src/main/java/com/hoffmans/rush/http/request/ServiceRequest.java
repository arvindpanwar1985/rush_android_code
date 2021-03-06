package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.ConfirmServiceBean;
import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.bean.RecordBean;
import com.hoffmans.rush.bean.ScheduledBean;
import com.hoffmans.rush.bean.ServiceBean;
import com.hoffmans.rush.bean.ServiceDetailBean;
import com.hoffmans.rush.bean.TrackDriverBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.model.EstimateServiceParams;
import com.hoffmans.rush.model.RatingParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 17/1/17.
 */

public class ServiceRequest extends BaseRequest {
    /**
     *APi request to estimate particular service
     * @param header
     * @param estimateServiceParams
     * @param apiCallback
     */
    public void estimateService(String header, EstimateServiceParams estimateServiceParams, final ApiCallback apiCallback){
        Call<ResponseBody> estimateCall=getAPIClient().estimateService(header,estimateServiceParams);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(estimateCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        ServiceBean bean = getGsonBuilder().fromJson(data, ServiceBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);
                    } else {
                       apiCallback.onRequestFailed(message);
                    }
                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }

        });
    }


    /**
     * App call to confirm any order
     * @param header
     * @param estimateServiceParams
     * @param apiCallback
     */

    public void confirmService(String header, EstimateServiceParams estimateServiceParams, final ApiCallback apiCallback){

        Call<ResponseBody> confirmCall=getAPIClient().confirmService(header,estimateServiceParams);

        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(confirmCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();

                        ConfirmServiceBean bean = getGsonBuilder().fromJson(data, ConfirmServiceBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }


        });
    }

    /**
     * get records
     * @param token
     * @param params
     * @param apiCallback
     */
    public void getRecords(String token, HashMap<String,String> params,final ApiCallback apiCallback){

        Call<ResponseBody> call=getAPIClient().getRecords(token,params);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        RecordBean bean = getGsonBuilder().fromJson(data, RecordBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }


        });
    }

    /**
     * set the service status to runnning /accepted /cancelled
     * @param auth
     * @param service_id
     * @param service_status
     * @param apiCallback
     */
    public void setServiceStatus(String auth,String service_id,String service_status,final  ApiCallback apiCallback){
        Call<ResponseBody> call=getAPIClient().setServiceStatus(auth,service_id,service_status);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        ScheduledBean scheduledBean=getGsonBuilder().fromJson(data,ScheduledBean.class);
                        scheduledBean.setMessage(message);
                        apiCallback.onRequestSuccess(scheduledBean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }
                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }
        });
    }

    /**
     * get the detail of service
     * @param auth
     * @param url
     * @param apiCallback
     */
    public void getServiceStatus(String auth,String url ,final ApiCallback apiCallback){
        Call<ResponseBody> call=getAPIClient().getServiceStatus(auth,url);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {

                        String data = obj.getJSONObject(DATA).toString();
                        ServiceDetailBean bean = getGsonBuilder().fromJson(data, ServiceDetailBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }
        });
    }



    public void getUpcomingServices(String auth ,String page,String perpage,String state,final ApiCallback apiCallback){
        Call<ResponseBody> call=getAPIClient().getUpcomingServices(auth,page,perpage,state);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        ScheduledBean bean = getGsonBuilder().fromJson(data, ScheduledBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }
        });
    }

    public void getTrackDriverDetails(String auth,String serviceId, final ApiCallback apiCallback){
        Call<ResponseBody> call=getAPIClient().getTrackDriver(auth,Integer.parseInt(serviceId));
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());

                    boolean status = obj.getBoolean(SUCCESS);
                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }

                    if (status) {

                        String data = obj.getJSONObject(DATA).toString();
                        TrackDriverBean bean = getGsonBuilder().fromJson(data, TrackDriverBean.class);
                        bean.setMessage(message);
                        apiCallback.onRequestSuccess(bean);

                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWebStatusFalse(String message) {

            }
        });


    }

    public void setServiceRating(String auth ,RatingParam ratingParam,final ApiCallback apiCallback){
        Call<ResponseBody> call=getAPIClient().rateDriver(auth, ratingParam);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);
                    String message,msg,msg1="";
                    msg=obj.getString(MESSAGE);
                    if(!obj.has(SPANISH_MESSAGE)){
                        message=msg;
                    }else {
                        msg1 = obj.getString(SPANISH_MESSAGE);
                        message=parseMessageUsingLocale(msg,msg1);
                    }
                    if (status) {
                        MessageBean messageBean=new MessageBean();
                        messageBean.setMessage(message);
                        apiCallback.onRequestSuccess(messageBean);
                    } else {
                        apiCallback.onRequestFailed(message);
                    }

                }catch (Exception e){
                    apiCallback.onRequestFailed(e.getMessage());
                }
            }

            @Override
            public void onWebStatusFalse(String message) {
                apiCallback.onRequestFailed(message);
            }
        });

    }



}
