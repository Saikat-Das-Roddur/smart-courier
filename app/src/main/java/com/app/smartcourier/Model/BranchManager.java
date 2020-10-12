package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class BranchManager {
    @SerializedName("name") private String name;
    @SerializedName("contact") private String contact;
    @SerializedName("email") private String email;
    @SerializedName("password") private String password;
    @SerializedName("location") private String location;
    @SerializedName("branchName") private String branchName;
    @SerializedName("value") private String value;
    @SerializedName("message") private String massage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    @Override
    public String toString() {
        return "BranchManager{" +
                "name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", branchName='" + branchName + '\'' +
                ", value='" + value + '\'' +
                ", massage='" + massage + '\'' +
                '}';
    }
}
