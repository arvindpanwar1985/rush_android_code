package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by devesh on 27/12/16.
 */

public class UserLocation implements Parcelable {

    private String city;
    private String country;
    private String state;
    private double longitude;
    private double latitude;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserLocation() {
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    protected UserLocation(Parcel in) {
        this.city = in.readString();
        this.country = in.readString();
        this.state = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static final Creator<UserLocation> CREATOR = new Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel source) {
            return new UserLocation(source);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };
}
