package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 24/1/17.
 */

public class VechileDetail implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;




    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;


    private String vehicle_type;
    private int vehicle_type_id;




    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
        dest.writeString(this.vehicle_type);
        dest.writeInt(this.vehicle_type_id);
    }

    public VechileDetail() {
    }

    protected VechileDetail(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
        this.vehicle_type = in.readString();
        this.vehicle_type_id = in.readInt();
    }

    public static final Creator<VechileDetail> CREATOR = new Creator<VechileDetail>() {
        @Override
        public VechileDetail createFromParcel(Parcel source) {
            return new VechileDetail(source);
        }

        @Override
        public VechileDetail[] newArray(int size) {
            return new VechileDetail[size];
        }
    };
}