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

    private boolean is_favorite;

    private String address_label;

    private int id;
    private int user_id;



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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isFavorite() {
        return is_favorite;
    }

    public void setFavorite(boolean favorite) {
        is_favorite = favorite;
    }

    public String getAddress_label() {
        return address_label;
    }

    public void setAddress_label(String address_label) {
        this.address_label = address_label;
    }

    public PickDropAddress() {
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
        dest.writeByte(this.is_favorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.address_label);
        dest.writeInt(this.id);
        dest.writeInt(this.user_id);

    }

    protected PickDropAddress(Parcel in) {
        this.streetAddress = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.is_favorite = in.readByte() != 0;
        this.address_label = in.readString();
        this.id = in.readInt();
        this.user_id = in.readInt();

    }

    public static final Creator<PickDropAddress> CREATOR = new Creator<PickDropAddress>() {
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