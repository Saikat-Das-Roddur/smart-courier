package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("parcel") private Parcel parcel;
    @SerializedName("amount") private String amount;
    @SerializedName("bkash_trx_id") private String bkash_trx_id;
    @SerializedName("bkash_number") private String bkash_number;
    @SerializedName("tracking_id") private String tracking_id;
    @SerializedName("contact_no") private String contact_no;
    @SerializedName("payment_status") private String paymentStatus;
    @SerializedName("branch") private String branch;
    @SerializedName("value") private String value;
    @SerializedName("message") private String message;
    @SerializedName("paymentMonth") private String paymentMonth;
    @SerializedName("paymentDate") private String paymentDate;
    @SerializedName("paymentTime") private String paymentTime;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBkash_trx_id() {
        return bkash_trx_id;
    }

    public void setBkash_trx_id(String bkash_trx_id) {
        this.bkash_trx_id = bkash_trx_id;
    }

    public String getBkash_number() {
        return bkash_number;
    }

    public void setBkash_number(String bkash_number) {
        this.bkash_number = bkash_number;
    }

    public String getTracking_id() {
        return tracking_id;
    }

    public void setTracking_id(String tracking_id) {
        this.tracking_id = tracking_id;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "parcel=" + parcel +
                ", amount='" + amount + '\'' +
                ", bkash_trx_id='" + bkash_trx_id + '\'' +
                ", bkash_number='" + bkash_number + '\'' +
                ", tracking_id='" + tracking_id + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", branch='" + branch + '\'' +
                ", value='" + value + '\'' +
                ", message='" + message + '\'' +
                ", paymentMonth='" + paymentMonth + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                '}';
    }
}
