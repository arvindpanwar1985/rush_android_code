package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 23/12/16.
 */

public class User implements Parcelable {



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("token")
    @Expose
    private String token;

    private String uid;

    private String provider;

    private String pic_url;

    private boolean is_email_verified;

    private boolean is_card_verified;

    private String bt_token;
    private UserLocation location;

    private boolean isSocialProvider;

    private int currency_symbol_id;


    public boolean isSocialProvider() {
        return isSocialProvider;
    }

    public void setSocialProvider(boolean socialProvider) {
        isSocialProvider = socialProvider;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean is_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public boolean is_card_verfied() {
        return is_card_verified;
    }

    public void setIs_card_verfied(boolean is_card_verfied) {
        this.is_card_verified = is_card_verfied;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public User() {
    }

    public String getBt_token() {
        return bt_token;
    }

    public void setBt_token(String bt_token) {
        this.bt_token = bt_token;
    }


    public int getCurrency_symbol_id() {
        return currency_symbol_id;
    }

    public void setCurrency_symbol_id(int currency_symbol_id) {
        this.currency_symbol_id = currency_symbol_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.status);
        dest.writeString(this.name);
        dest.writeString(this.role);
        dest.writeString(this.token);
        dest.writeString(this.uid);
        dest.writeString(this.provider);
        dest.writeString(this.pic_url);
        dest.writeByte(this.is_email_verified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_card_verified ? (byte) 1 : (byte) 0);
        dest.writeString(this.bt_token);
        dest.writeParcelable(this.location, flags);
        dest.writeByte(this.isSocialProvider ? (byte) 1 : (byte) 0);
        dest.writeInt(this.currency_symbol_id);
    }

    protected User(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.email = in.readString();
        this.phone = in.readString();
        this.status = in.readString();
        this.name = in.readString();
        this.role = in.readString();
        this.token = in.readString();
        this.uid = in.readString();
        this.provider = in.readString();
        this.pic_url = in.readString();
        this.is_email_verified = in.readByte() != 0;
        this.is_card_verified = in.readByte() != 0;
        this.bt_token = in.readString();
        this.location = in.readParcelable(UserLocation.class.getClassLoader());
        this.isSocialProvider = in.readByte() != 0;
        this.currency_symbol_id = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
