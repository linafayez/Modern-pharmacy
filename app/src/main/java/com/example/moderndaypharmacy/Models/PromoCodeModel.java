package com.example.moderndaypharmacy.Models;

import com.google.firebase.Timestamp;

public class PromoCodeModel {
    private String Id;
    private Timestamp time;
    private Timestamp EndTime;
    private String code;
    private int discount;

    public PromoCodeModel(String id, Timestamp time, Timestamp endTime, String code, int discount) {
        Id = id;
        this.time = time;
        EndTime = endTime;
        this.code = code;
        this.discount = discount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getEndTime() {
        return EndTime;
    }

    public void setEndTime(Timestamp endTime) {
        EndTime = endTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
