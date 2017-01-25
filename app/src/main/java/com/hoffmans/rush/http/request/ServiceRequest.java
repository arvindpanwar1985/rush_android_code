package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.ConfirmServiceBean;
import com.hoffmans.rush.bean.RecordBean;
import com.hoffmans.rush.bean.ServiceBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.model.EstimateServiceParams;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 17/1/17.
 */

public class ServiceRequest extends BaseRequest {



    public void estimateService(String header, EstimateServiceParams estimateServiceParams, final ApiCallback apiCallback){
        Call<ResponseBody> estimateCall=getAPIClient().estimateService(header,estimateServiceParams);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(estimateCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message=obj.getString(MESSAGE);
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




    public void confirmService(String header, EstimateServiceParams estimateServiceParams, final ApiCallback apiCallback){
        Call<ResponseBody> confirmCall=getAPIClient().confirmService(header,estimateServiceParams);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(confirmCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message=obj.getString(MESSAGE);
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


    public void getRecords(String token, HashMap<String,String> params,final ApiCallback apiCallback){

        Call<ResponseBody> call=getAPIClient().getRecords(token,params);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(call);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message=obj.getString(MESSAGE);
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
}
