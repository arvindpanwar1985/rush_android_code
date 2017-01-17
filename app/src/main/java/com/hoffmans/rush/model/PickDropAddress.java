package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 16/1/17.
 */

public class PickDropAddress implements Parcelable {

    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.streetAddress);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    public PickDropAddress() {
    }

    protected PickDropAddress(Parcel in) {
        this.streetAddress = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<PickDropAddress> CREATOR = new Parcelable.Creator<PickDropAddress>() {
        @Override
        public PickDropAddress createFromParcel(Parcel source) {
            return new PickDropAddress(source);
        }

        @Override
        public PickDropAddress[] newArray(int size) {
            return new PickDropAddress[size];
        }
    };
}