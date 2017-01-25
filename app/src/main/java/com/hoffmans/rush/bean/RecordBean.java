package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.Record;

import java.util.List;

/**
 * Created by devesh on 24/1/17.
 */

public class RecordBean extends BaseBean {

    private List<Record> services;
    private int total_items;

    public List<Record> getRecords() {
        return services;
    }

    public void setREcords(List<Record> services) {
        this.services = services;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }
}
