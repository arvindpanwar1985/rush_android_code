package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.PickDropAddress;

import java.util.ArrayList;

/**
 * Created by devesh on 18/1/17.
 */

public class FavouriteBean extends BaseBean {

    private ArrayList<PickDropAddress> addresses;

    public ArrayList<PickDropAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<PickDropAddress> addresses) {
        this.addresses = addresses;
    }
}
