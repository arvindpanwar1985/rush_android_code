package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.ConfirmService;

/**
 * Created by devesh on 24/1/17.
 */

public class ConfirmServiceBean extends BaseBean {

 private ConfirmService service;

    public ConfirmService getService() {
        return service;
    }

    public void setService(ConfirmService service) {
        this.service = service;
    }
}
