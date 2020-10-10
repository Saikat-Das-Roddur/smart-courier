package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class Branch {
    @SerializedName("user") private User user;
    @SerializedName("branchName") private String branchName;
    @SerializedName("branchContact") private String branchContact;
    @SerializedName("branchLocation") private String branchLocation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchContact() {
        return branchContact;
    }

    public void setBranchContact(String branchContact) {
        this.branchContact = branchContact;
    }

    public String getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }
}
