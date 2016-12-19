package com.hoffmans.rush.bean;

@SuppressWarnings("serial")
public class BaseBean {

    public transient String statusMessage;
    public transient int statusCode;
    public transient String statusMsg;
    private transient String progressMsg = "Please wait...";
    private transient boolean progressEnable = true;
    public String getProgressMsg() {
        return progressMsg;
    }

    public void setProgressMsg(String progressMsg) {
        this.progressMsg = progressMsg;
    }

    public boolean isProgressEnable() {
        return progressEnable;
    }

    public void setProgressEnable(boolean enbale) {
        progressEnable = enbale;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }


}
