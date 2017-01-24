package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 24/1/17.
 */

public class DateTime implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.time);
    }

    public DateTime() {
    }

    protected DateTime(Parcel in) {
        this.date = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<DateTime> CREATOR = new Parcelable.Creator<DateTime>() {
        @Override
        public DateTime createFromParcel(Parcel source) {
            return new DateTime(source);
        }

        @Override
        public DateTime[] newArray(int size) {
            return new DateTime[size];
        }
    };
}