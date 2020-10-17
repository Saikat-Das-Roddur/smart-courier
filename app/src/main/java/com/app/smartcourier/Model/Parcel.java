package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class Parcel {
    @SerializedName("title") private String title;
    @SerializedName("description") private String description;
    @SerializedName("contact_no") private String contact_no;
    @SerializedName("tracking_id") private String tracking_id;
    @SerializedName("location") private String location;
    @SerializedName("latitude") private String latitude;
    @SerializedName("longitude") private String longitude;
    @SerializedName("payment_method") private String payment_method;
    @SerializedName("branch") private String branch;
    @SerializedName( "dest_branch")  String dest_branch;
    @SerializedName("time") private String time;
    @SerializedName("date") private String date;

    @SerializedName("value") private String value;
    @SerializedName("message") private String massage;
    @SerializedName("created_at") private String created_at;
    @SerializedName("updated_at") private String updated_at;
    @SerializedName("image") private String image;
    @SerializedName("status") private String parcelStatus;

    @Override
    public String toString() {
        return "Parcel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", tracking_id='" + tracking_id + '\'' +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", payment_method='" + payment_method + '\'' +
                ", branch='" + branch + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", value='" + value + '\'' +
                ", massage='" + massage + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", image='" + image + '\'' +
                ", parcelStatus='" + parcelStatus + '\'' +
                '}';
    }

    public String getDest_branch() {
        return dest_branch;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getTracking_id() {
        return tracking_id;
    }

    public void setTracking_id(String tracking_id) {
        this.tracking_id = tracking_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(String parcelStatus) {
        this.parcelStatus = parcelStatus;
    }
}
