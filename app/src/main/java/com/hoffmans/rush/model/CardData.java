package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 13/1/17.
 */

public class CardData implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.cardholderName);
        dest.writeString(this.expirationMonth);
        dest.writeString(this.expirationYear);
        dest.writeString(this.last4);
        dest.writeString(this.cardType);
        dest.writeString(this.imageUrl);
        dest.writeValue(this._default);
    }

    public CardData() {
    }

    protected CardData(Parcel in) {
        this.token = in.readString();
        this.cardholderName = in.readString();
        this.expirationMonth = in.readString();
        this.expirationYear = in.readString();
        this.last4 = in.readString();
        this.cardType = in.readString();
        this.imageUrl = in.readString();
        this._default = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CardData> CREATOR = new Parcelable.Creator<CardData>() {
        @Override
        public CardData createFromParcel(Parcel source) {
            return new CardData(source);
        }

        @Override
        public CardData[] newArray(int size) {
            return new CardData[size];
        }
    };
}