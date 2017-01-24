package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by devesh on 24/1/17.
 */

public class ConfirmService  implements Parcelable {


    private String date;
    private int vehicle_type_id;
    private VechileDetail vehicle_details;
    private PickDropAddress pick_address;
    private List<PickDropAddress> drop_addresses;
    private TransactionDetails transactionDetails;
    private DateTime date_time;




    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public PickDropAddress getPick_address() {
        return pick_address;
    }

    public void setPick_address(PickDropAddress pick_address) {
        this.pick_address = pick_address;
    }

    public List<PickDropAddress> getDrop_addresses() {
        return drop_addresses;
    }

    public void setDrop_addresses(List<PickDropAddress> drop_addresses) {
        this.drop_addresses = drop_addresses;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeInt(this.vehicle_type_id);

        dest.writeParcelable(this.pick_address, flags);
        dest.writeTypedList(this.drop_addresses);

    }

    protected ConfirmService(Parcel in) {
        this.date = in.readString();
        this.vehicle_type_id = in.readInt();

        this.pick_address = in.readParcelable(PickDropAddress.class.getClassLoader());
        this.drop_addresses = in.createTypedArrayList(PickDropAddress.CREATOR);

    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };


}
