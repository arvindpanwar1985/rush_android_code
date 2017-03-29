package com.hoffmans.rush.bean;

@SuppressWarnings("serial")
public class BaseBean {

    public transient String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
