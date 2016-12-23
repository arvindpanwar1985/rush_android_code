package com.hoffmans.rush.listners;

import okhttp3.ResponseBody;

public interface LoginCallBack {
    public void onLoginSuccess(ResponseBody body);
    public void onLoginFailed(String message);


}