package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class OtherInfo {
    @SerializedName("user")  String user;
    @SerializedName("manager")  String manager;

    public String getUser() {
        return user;
    }

    public String getManager() {
        return manager;
    }

    @Override
    public String toString() {
        return "OtherInfo{" +
                "user='" + user + '\'' +
                ", manager='" + manager + '\'' +
                '}';
    }
}
