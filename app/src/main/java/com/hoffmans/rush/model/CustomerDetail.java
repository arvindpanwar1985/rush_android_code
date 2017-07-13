package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 14/3/17.
 */

public class CustomerDetail implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("pic_url")
    @Expose
    private String picUrl;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.timeZone);
        dest.writeString(this.phone);
        dest.writeString(this.updatedAt);
        dest.writeString(this.status);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.createdAt);
        dest.writeString(this.picUrl);
    }

    public CustomerDetail() {
    }

    protected CustomerDetail(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.timeZone = in.readString();
        this.phone = in.readString();
        this.updatedAt = in.readString();
        this.status = in.readString();
        this.email = in.readString();
        this.name = in.readString();
        this.createdAt = in.readString();
        this.picUrl = in.readString();
    }

    public static final Creator<CustomerDetail> CREATOR = new Creator<CustomerDetail>() {
        @Override
        public CustomerDetail createFromParcel(Parcel source) {
            return new CustomerDetail(source);
        }

        @Override
        public CustomerDetail[] newArray(int size) {
            return new CustomerDetail[size];
        }
    };
}
