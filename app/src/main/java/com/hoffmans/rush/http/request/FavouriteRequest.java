package com.hoffmans.rush.http.request;

import com.hoffmans.rush.bean.FavouriteBean;
import com.hoffmans.rush.http.ConnectionManager;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.BaseListener;
import com.hoffmans.rush.model.AddFavouriteBody;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by devesh on 18/1/17.
 */

public class FavouriteRequest extends BaseRequest {


    public void addToFavourite(String token, AddFavouriteBody body, final ApiCallback callback){
        Call<ResponseBody> favCall=getAPIClient().createfavoriteAddress(token,body);
        ConnectionManager connectionManager=ConnectionManager.getConnectionInstance(favCall);
        connectionManager.callApi(new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody responseBody) {
                try {
                    JSONObject obj=new JSONObject(responseBody.string());
                    boolean status = obj.getBoolean(SUCCESS);

                    String message=obj.getString(MESSAGE);
                    if (status) {
                        String data = obj.getJSONObject(DATA).toString();
                        FavouriteBean bean = getGsonBuilder().fromJson(data, FavouriteBean.class);
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
