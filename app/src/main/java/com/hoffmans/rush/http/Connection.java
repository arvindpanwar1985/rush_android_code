package com.hoffmans.rush.http;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.utils.ErrorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by devesh on 16/12/16.
 */

public class Connection  {
    private ProgressDialog mProgressDialog;
    private BaseBean mObject;
    private static final String DATA="data";
    private static final String MESSAGE="message";
    private static final String STATUS="status";
    private Gson mGson;

    public Connection(final Context context, Call<ResponseBody>call, final WebserviceType webserviceType, BaseBean object, final BaseListener.OnWebServiceCompleteListener mListener){

         this.mObject=object;
         mGson = new GsonBuilder().create();
         if (mObject.isProgressEnable()) {
            this.mProgressDialog = ProgressDialog.show(context, null, mObject.getProgressMessage());
            this.mProgressDialog.setCancelable(false);

         }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if (mObject.isProgressEnable()) {
                        if(mProgressDialog.isShowing()) {
                            try{
                                mProgressDialog.dismiss();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                            mObject = getResponse(webserviceType, response.body());
                            if (mListener != null && mObject != null) {
                                mListener.onWebServiceComplete(mObject,response.code());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mListener.onWebServiceError(e,response.code());
                            return;
                        } catch (ClassCastException e) {
                            mListener.onWebServiceError(e,response.code());
                            return;
                        } catch (Exception e) {
                            mListener.onWebServiceError(e,response.code());
                            e.printStackTrace();
                            return;
                        }
                        if (mObject == null) {
                            mListener.onWebServiceError(new NullPointerException("No Data Found"),response.code());
                        }
                }else{
                    APIError error = ErrorUtils.parseError(response);
                    Exception exception =new Exception(error.message());
                    mListener.onWebServiceError(exception,response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mObject.isProgressEnable()) {
                    if(mProgressDialog.isShowing()) {
                        try{
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                mListener.onWebStatusFalse(t.getMessage());
            }
        });
    }




    private BaseBean getResponse(WebserviceType type, ResponseBody responseBody) throws  Exception {

        String stringResponse=responseBody.string();
        JSONObject obj = new JSONObject(stringResponse);
        boolean status = obj.getBoolean(STATUS);
        if (status) {
            String data = obj.getJSONObject(DATA).toString();
            String message=obj.getString(MESSAGE);
            switch (type) {
                case LOGIN:

                    return null;



            }
        } else {
            throw new Exception(obj.getString(MESSAGE));
        }
        return null;
    }


}
