package com.hoffmans.rush.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hoffmans.rush.model.Currency;

import java.util.List;

/**
 * Created by devesh on 6/1/17.
 */

public class CurrencyBean extends BaseBean{

    @SerializedName("currencies")
    @Expose
    private List<Currency> currencies = null;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

}