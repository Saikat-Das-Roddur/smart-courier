package com.app.smartcourier.Model;

public class History {
    private Parcel parcel;
    private Payment payment;
    private String title;
    private String description;
    private String status;

    public Parcel getParcel() {
        return parcel;
    }

    public Payment getPayment() {
        return payment;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
