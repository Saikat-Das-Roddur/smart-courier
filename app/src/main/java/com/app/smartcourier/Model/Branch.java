package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class Branch {
    @SerializedName("branch_name") private String branchName;
    @SerializedName("branch_contact") private String branchContact;
    @SerializedName("branch_location") private String branchLocation;

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
