package com.hoffmans.rush.bean;

/**
 * Created by devesh on 30/12/16.
 */

public class MessageBean  extends BaseBean {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
