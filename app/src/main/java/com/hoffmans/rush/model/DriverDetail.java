package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 24/1/17.
 */

public class DriverDetail implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pic_url")
    @Expose
    private String picUrl;

    @SerializedName("color")
    @Expose
    private String color;

    private String color_es;

    public String getColor_es() {
        return color_es;
    }





    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    @SerializedName("plate_number")
    @Expose
    private String plate_number;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public DriverDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.status);
        dest.writeString(this.timeZone);
        dest.writeString(this.name);
        dest.writeString(this.picUrl);
        dest.writeString(this.color);
        dest.writeString(this.color_es);
        dest.writeString(this.plate_number);
    }

    protected DriverDetail(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.email = in.readString();
        this.phone = in.readString();
        this.status = in.readString();
        this.timeZone = in.readString();
        this.name = in.readString();
        this.picUrl = in.readString();
        this.color = in.readString();
        this.color_es = in.readString();
        this.plate_number = in.readString();
    }

    public static final Creator<DriverDetail> CREATOR = new Creator<DriverDetail>() {
        @Override
        public DriverDetail createFromParcel(Parcel source) {
            return new DriverDetail(source);
        }

        @Override
        public DriverDetail[] newArray(int size) {
            return new DriverDetail[size];
        }
    };
}