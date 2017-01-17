package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.ServiceBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.model.EstimateServiceParams;

import org.json.JSONObject;

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
}
