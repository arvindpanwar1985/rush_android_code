package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arvind on 31/5/17.
 */

public class Rate implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.rating);
        dest.writeString(this.customer_id);
    }

    public Rate() {
    }

    protected Rate(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.rating = in.readString();
        this.customer_id = in.readString();
    }

    public static final Creator<Rate> CREATOR = new Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };
}
