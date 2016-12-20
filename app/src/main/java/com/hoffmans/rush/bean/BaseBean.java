package com.hoffmans.rush.bean;

@SuppressWarnings("serial")
public class BaseBean {


    public transient String message;
    private transient String progressMessage = "Please wait...";
    public boolean isProgressEnable() {
        return progressEnable;
    }

    public void setProgressEnable(boolean progressEnable) {
        this.progressEnable = progressEnable;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private transient boolean progressEnable = true;






}
