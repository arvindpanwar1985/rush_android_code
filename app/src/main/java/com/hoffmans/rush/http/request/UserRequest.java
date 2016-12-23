package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.User;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 23/12/16.
 */

public class UserRequest extends BaseRequest {

    /**
     *
     * @param params required params
     * @param callback api callback to give data to activity and fragment
     * @param baseBean type of bean
     */
    public void createUser(String params, final ApiCallback callback, final BaseBean baseBean){

        Call<ResponseBody> createUserCall=getAPIClient().createUser(params);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(createUserCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(STATUS);
                    String message=obj.getString(MESSAGE);
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        baseBean.setMessage(message);
                        User login = getGsonBuilder().fromJson(data, User.class);
                        callback.onRequestSuccess(login);
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
