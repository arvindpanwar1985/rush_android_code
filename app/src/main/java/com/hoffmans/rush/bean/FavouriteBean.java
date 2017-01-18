package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.PickDropAddress;

import java.util.List;

/**
 * Created by devesh on 18/1/17.
 */

public class FavouriteBean extends BaseBean {

    private List<PickDropAddress> addresses;

    public List<PickDropAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PickDropAddress> addresses) {
        this.addresses = addresses;
    }
}
