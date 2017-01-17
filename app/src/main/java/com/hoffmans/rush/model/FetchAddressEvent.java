package com.hoffmans.rush.model;

/**
 * Created by devesh on 17/1/17.
 */

public class FetchAddressEvent {
    private boolean sucess;
    private Service service;

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
