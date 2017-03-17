package com.hoffmans.rush.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by devesh on 15/3/17.
 */

public class ServiceData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("payment_received")
    @Expose
    private Boolean paymentReceived;
    @SerializedName("date_time")
    @Expose
    private DateTime dateTime;

    @SerializedName("estimate")
    @Expose
    private Estimate estimate;

    @SerializedName("vehicle_details")
    @Expose
    private VechileDetail vechileDetail;


    @SerializedName("drop_down")
    @Expose
    private List<PickDropAddress> dropAddressList;


    @SerializedName("pick_up")
    @Expose
    private PickDropAddress picAddress;

    @SerializedName("customer_details")
    @Expose
    private CustomerDetail customerDetail;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(Boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public VechileDetail getVechileDetail() {
        return vechileDetail;
    }

    public List<PickDropAddress> getDropAddressList() {
        return dropAddressList;
    }

    public PickDropAddress getPicAddress() {
        return picAddress;
    }

    public CustomerDetail getCustomerDetail() {
        return customerDetail;
    }
}
