package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devesh on 16/1/17.
 */

public class Service implements Parcelable {


    private String date;
    private int vehicle_type_id;
    private PickDropAddress pick_address;
    private List<PickDropAddress> drop_addresses;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeInt(this.vehicle_type_id);
        dest.writeParcelable(this.pick_address, flags);
        dest.writeList(this.drop_addresses);
    }

    public Service() {
    }

    protected Service(Parcel in) {
        this.date = in.readString();
        this.vehicle_type_id = in.readInt();
        this.pick_address = in.readParcelable(PickDropAddress.class.getClassLoader());
        this.drop_addresses = new ArrayList<PickDropAddress>();
        in.readList(this.drop_addresses, PickDropAddress.class.getClassLoader());
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
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
