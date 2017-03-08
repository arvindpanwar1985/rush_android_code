package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.ConfirmServiceBean;
import com.hoffmans.rush.bean.MessageBean;
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

                    String message=obj.getString(MESSAGE);
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
