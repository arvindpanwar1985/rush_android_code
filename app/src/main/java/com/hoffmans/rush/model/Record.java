package com.hoffmans.rush.model;

/**
 * Created by devesh on 24/1/17.
 */

public class Record {

 private DateTime date_time;
 private DriverDetail driver_details;
 private Estimate estimate;
 private VechileDetail vehicle_details;
 private PickDropAddress pick_up;

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
}
