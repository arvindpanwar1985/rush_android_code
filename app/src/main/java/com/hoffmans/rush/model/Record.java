package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by devesh on 24/1/17.
 */

public class Record implements Parcelable {




    private DateTime date_time;
    private DriverDetail driver_details;
    private Estimate estimate;
    private VechileDetail vehicle_details;
    private PickDropAddress pick_up;
    private List<PickDropAddress>drop_down;
    private CustomerDetail customer_details;
    private String comment;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    private Rating rate;

    public void setState(String state) {
        this.state = state;
    }





    public Rating getRate() {
        return rate;

    }

    public void setRate(Rating rate) {
        this.rate = rate;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
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


    public Record() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.date_time, flags);
        dest.writeParcelable(this.driver_details, flags);
        dest.writeParcelable(this.estimate, flags);
        dest.writeParcelable(this.vehicle_details, flags);
        dest.writeParcelable(this.pick_up, flags);
        dest.writeTypedList(this.drop_down);
        dest.writeParcelable(this.customer_details, flags);
        dest.writeString(this.comment);
        dest.writeString(this.state);
        dest.writeString(this.id);
        dest.writeParcelable(this.rate, flags);
    }

    protected Record(Parcel in) {
        this.date_time = in.readParcelable(DateTime.class.getClassLoader());
        this.driver_details = in.readParcelable(DriverDetail.class.getClassLoader());
        this.estimate = in.readParcelable(Estimate.class.getClassLoader());
        this.vehicle_details = in.readParcelable(VechileDetail.class.getClassLoader());
        this.pick_up = in.readParcelable(PickDropAddress.class.getClassLoader());
        this.drop_down = in.createTypedArrayList(PickDropAddress.CREATOR);
        this.customer_details = in.readParcelable(CustomerDetail.class.getClassLoader());
        this.comment = in.readString();
        this.state = in.readString();
        this.id = in.readString();
        this.rate = in.readParcelable(Rating.class.getClassLoader());
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
