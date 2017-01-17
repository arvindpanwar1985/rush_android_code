package com.hoffmans.rush.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 17/1/17.
 */

public class Estimate {

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

}