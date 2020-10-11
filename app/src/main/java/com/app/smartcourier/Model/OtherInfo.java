package com.app.smartcourier.Model;

import com.google.gson.annotations.SerializedName;

public class OtherInfo {
    @SerializedName("contact")  String contact;

    @Override
    public String toString() {
        return "OtherInfo{" +
                "contact='" + contact + '\'' +
                '}';
    }
}
