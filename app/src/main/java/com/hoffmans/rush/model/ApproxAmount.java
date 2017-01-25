package com.hoffmans.rush.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 24/1/17.
 */

public class ApproxAmount {

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("symbol")
    @Expose
    private String symbol;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}