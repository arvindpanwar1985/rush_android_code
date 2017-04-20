package com.hoffmans.rush.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hoffmans.rush.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devesh on 14/4/17.
 */

public class NeaabyDriversBean extends BaseBean {


    private  boolean foundDrivers;
    @SerializedName("drivers")
    @Expose
    private List<User> listNearbyDrivers=new ArrayList<>();


    public List<User> getListNearbyDrivers() {
        return listNearbyDrivers;
    }


    public boolean isFoundDrivers() {
        return foundDrivers;
    }

    public void setFoundDrivers(boolean foundDrivers) {
        this.foundDrivers = foundDrivers;
    }
}
