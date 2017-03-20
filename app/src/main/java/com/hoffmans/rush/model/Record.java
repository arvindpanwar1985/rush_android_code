package com.hoffmans.rush.model;

import java.util.List;

/**
 * Created by devesh on 24/1/17.
 */

public class Record {

 private String state;
 private DateTime date_time;
 private DriverDetail driver_details;
 private Estimate estimate;
 private VechileDetail vehicle_details;
 private PickDropAddress pick_up;
 private List<PickDropAddress>drop_down;
    private CustomerDetail customer_details;

    public DateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(DateTime date_time) {
        this.date_time = date_time;
    }

    public DriverDetail getDriver_details() {
        return driver_details;
    }

    public void setDriver_details(DriverDetail driver_details) {
        this.driver_details = driver_details;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public VechileDetail getVehicle_details() {
        return vehicle_details;
    }

    public void setVehicle_details(VechileDetail vehicle_details) {
        this.vehicle_details = vehicle_details;
    }

    public PickDropAddress getPick_up() {
        return pick_up;
    }

    public void setPick_up(PickDropAddress pick_up) {
        this.pick_up = pick_up;
    }

    public List<PickDropAddress> getDrop_down() {
        return drop_down;
    }

    public CustomerDetail getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(CustomerDetail customer_details) {
        this.customer_details = customer_details;
    }

    public void setDrop_down(List<PickDropAddress> drop_down) {
        this.drop_down = drop_down;

    }

    public String getState() {
        return state;
    }
}
