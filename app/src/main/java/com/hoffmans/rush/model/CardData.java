package com.hoffmans.rush.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 13/1/17.
 */

public class CardData {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("cardholder_name")
    @Expose
    private String cardholderName;
    @SerializedName("expiration_month")
    @Expose
    private String expirationMonth;
    @SerializedName("expiration_year")
    @Expose
    private String expirationYear;
    @SerializedName("last_4")
    @Expose
    private String last4;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("default")
    @Expose
    private Boolean _default;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getDefault() {
        return _default;
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }

}