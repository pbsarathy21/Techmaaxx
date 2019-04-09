package com.android.ninos.techmaaxx.pojo.transaction_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Parthasarathy D on 1/5/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public class Data {


    @SerializedName("zeepay_id")
    @Expose
    private Integer zeepayId;
    @SerializedName("extr_id")
    @Expose
    private String extrId;
    @SerializedName("sender_first_name")
    @Expose
    private String senderFirstName;
    @SerializedName("sender_last_name")
    @Expose
    private String senderLastName;
    @SerializedName("sender_country")
    @Expose
    private String senderCountry;
    @SerializedName("recipient_first_name")
    @Expose
    private String recipientFirstName;
    @SerializedName("recipient_last_name")
    @Expose
    private String recipientLastName;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("mobile_account")
    @Expose
    private String mobileAccount;
    @SerializedName("fees")
    @Expose
    private Object fees;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("amount_sent")
    @Expose
    private String amountSent;
    @SerializedName("amount_payout")
    @Expose
    private Object amountPayout;
    @SerializedName("created_at")
    @Expose
    private CreatedAt createdAt;

    public Integer getZeepayId() {
        return zeepayId;
    }

    public void setZeepayId(Integer zeepayId) {
        this.zeepayId = zeepayId;
    }

    public String getExtrId() {
        return extrId;
    }

    public void setExtrId(String extrId) {
        this.extrId = extrId;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMobileAccount() {
        return mobileAccount;
    }

    public void setMobileAccount(String mobileAccount) {
        this.mobileAccount = mobileAccount;
    }

    public Object getFees() {
        return fees;
    }

    public void setFees(Object fees) {
        this.fees = fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getAmountSent() {
        return amountSent;
    }

    public void setAmountSent(String amountSent) {
        this.amountSent = amountSent;
    }

    public Object getAmountPayout() {
        return amountPayout;
    }

    public void setAmountPayout(Object amountPayout) {
        this.amountPayout = amountPayout;
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }

}

