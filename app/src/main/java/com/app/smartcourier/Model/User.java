package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name") private String name;
    @SerializedName("contact") private String contact;
    @SerializedName("email") private String email;
    @SerializedName("image") private String image;
    @SerializedName("password") private String password;
    @SerializedName("location") private String location;
    @SerializedName("isBranchManager") private int isBranchManager;

    @SerializedName("value") private String value;
    @SerializedName("message") private String massage;

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

    public int getIsBranchManager() {
        return isBranchManager;
    }

    public void setIsBranchManager(int isBranchManager) {
        this.isBranchManager = isBranchManager;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", isBranchManager='" + isBranchManager + '\'' +
                ", value='" + value + '\'' +
                ", massage='" + massage + '\'' +
                '}';
    }
}
