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

    @SerializedName("drivers")
    @Expose
    private List<User> listNearbyDrivers=new ArrayList<>();


    public List<User> getListNearbyDrivers() {
        return listNearbyDrivers;
    }
}
