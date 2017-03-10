package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by devesh on 24/1/17.
 */

public class ConfirmService implements Parcelable {


    private String date;
    private int vehicle_type_id;
    private VechileDetail vehicle_details;
    private PickDropAddress pick_up;
    private ArrayList<PickDropAddress> drop_down;
    private TransactionDetails transactionDetails;
    private DateTime date_time;
    private Estimate estimate ;




    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public PickDropAddress getPick_address() {
        return pick_up;
    }

    public void setPick_address(PickDropAddress pick_address) {
        this.pick_up = pick_address;
    }

    public ArrayList<PickDropAddress> getDrop_addresses() {
        return drop_down;
    }

    public void setDrop_addresses(ArrayList<PickDropAddress> drop_addresses) {
        this.drop_down = drop_addresses;
    }




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public VechileDetail getVehicle_details() {
        return vehicle_details;
    }

    public void setVehicle_details(VechileDetail vehicle_details) {
        this.vehicle_details = vehicle_details;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public DateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(DateTime date_time) {
        this.date_time = date_time;
    }




    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeInt(this.vehicle_type_id);

        dest.writeParcelable(this.pick_up, flags);
        dest.writeTypedList(this.drop_down);
        dest.writeParcelable(this.transactionDetails, flags);
        dest.writeParcelable(this.date_time, flags);
        dest.writeParcelable(this.estimate, flags);
    }

    public ConfirmService() {
    }

    protected ConfirmService(Parcel in) {
        this.date = in.readString();
        this.vehicle_type_id = in.readInt();

        this.pick_up = in.readParcelable(PickDropAddress.class.getClassLoader());
        this.drop_down = in.createTypedArrayList(PickDropAddress.CREATOR);
        this.transactionDetails = in.readParcelable(TransactionDetails.class.getClassLoader());
        this.date_time = in.readParcelable(DateTime.class.getClassLoader());
        this.estimate = in.readParcelable(Estimate.class.getClassLoader());
    }

    public static final Parcelable.Creator<ConfirmService> CREATOR = new Parcelable.Creator<ConfirmService>() {
        @Override
        public ConfirmService createFromParcel(Parcel source) {
            return new ConfirmService(source);
        }

        @Override
        public ConfirmService[] newArray(int size) {
            return new ConfirmService[size];
        }
    };
}
