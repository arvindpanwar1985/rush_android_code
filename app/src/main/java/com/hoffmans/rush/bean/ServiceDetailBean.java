package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.ServiceData;

/**
 * Created by devesh on 15/3/17.
 */

public class ServiceDetailBean extends BaseBean {
    private ServiceData service;

    public ServiceData getService() {
        return service;
    }

    public void setService(ServiceData service) {
        this.service = service;
    }
}
