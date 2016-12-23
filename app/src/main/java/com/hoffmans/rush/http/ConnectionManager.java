package com.hoffmans.rush.http;

import com.hoffmans.rush.listners.BaseListener;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by devesh on 23/12/16.
 */

public class ConnectionManager {
    static ConnectionManager mConnectionManger;
    private Call<ResponseBody> enqueueCall;

    public static ConnectionManager getConnectionInstance(Call<ResponseBody> call) {
        if (mConnectionManger == null) {
            mConnectionManger = new ConnectionManager();
            mConnectionManger.setEnqueueCall(call);
            return mConnectionManger;
        } else{
            mConnectionManger.setEnqueueCall(call);
            return mConnectionManger;
        }

    }


    public void setEnqueueCall(Call<ResponseBody> mCall) {
        this.enqueueCall = mCall;
    }

    public void callApi(final BaseListener.OnWebServiceCompleteListener mListener){
        this.enqueueCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    mListener.onWebServiceComplete(response.body());
                }else{
                    //APIError error = ErrorUtils.parseError(response);
                    mListener.onWebStatusFalse(response.message());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mListener.onWebStatusFalse(t.getMessage());
            }
        });
    }
}