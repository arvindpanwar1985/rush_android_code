package com.hoffmans.rush.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 17/1/17.
 */

public class Estimate implements Parcelable {

    @SerializedName("approx_converted_amount")
    @Expose
    private Double approxConvertedAmount;
    @SerializedName("approx_amount")
    @Expose
    private Double approxAmount;
    @SerializedName("approx_time")
    @Expose
    private String approxTime;
    @SerializedName("symbol")
    @Expose
    private String symbol;

    public Double getApproxConvertedAmount() {
        return approxConvertedAmount;
    }

    public void setApproxConvertedAmount(Double approxConvertedAmount) {
        this.approxConvertedAmount = approxConvertedAmount;
    }

    public Double getApproxAmount() {
        return approxAmount;
    }

    public void setApproxAmount(Double approxAmount) {
        this.approxAmount = approxAmount;
    }

    public String getApproxTime() {
        return approxTime;
    }

    public void setApproxTime(String approxTime) {
        this.approxTime = approxTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.approxConvertedAmount);
        dest.writeValue(this.approxAmount);
        dest.writeString(this.approxTime);
        dest.writeString(this.symbol);
    }

    public Estimate() {
    }

    protected Estimate(Parcel in) {
        this.approxConvertedAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.approxAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.approxTime = in.readString();
        this.symbol = in.readString();
    }

    public static final Creator<Estimate> CREATOR = new Creator<Estimate>() {
        @Override
        public Estimate createFromParcel(Parcel source) {
            return new Estimate(source);
        }

        @Override
        public Estimate[] newArray(int size) {
            return new Estimate[size];
        }
    };
}