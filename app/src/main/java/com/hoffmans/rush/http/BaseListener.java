package com.hoffmans.rush.http;


import com.hoffmans.rush.bean.BaseBean;

public interface BaseListener {

    interface OnWebServiceCompleteListener {

        void onWebServiceComplete(BaseBean baseObject, int statusCode);
        void onWebServiceError(Exception exception, int statusCode);
        // this method will fired when service status is false
        void onWebStatusFalse(String message);
    }


}