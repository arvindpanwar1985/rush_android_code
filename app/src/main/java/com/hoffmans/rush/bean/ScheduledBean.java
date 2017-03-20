package com.hoffmans.rush.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.model.ServiceData;

import java.util.List;

/**
 * Created by devesh on 17/3/17.
 */

public class ScheduledBean extends BaseBean {
    @SerializedName("upcoming_services")
    @Expose
    private List<Record> scheduledServices;
    @SerializedName("current_service")
    @Expose
    private ServiceData currentOrder;

    public List<Record> getScheduledServices() {
        return scheduledServices;
    }

    public ServiceData getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(ServiceData currentOrder) {
        this.currentOrder = currentOrder;
    }
}
