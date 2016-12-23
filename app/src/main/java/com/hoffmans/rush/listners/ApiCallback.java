package com.hoffmans.rush.listners;

import com.hoffmans.rush.bean.BaseBean;

/**
 * Created by devesh on 23/12/16.
 */

public interface ApiCallback {

    public void onRequestSuccess(BaseBean body);
    public void onRequestFailed(String message);
}
