package com.example.moderndaypharmacy;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OrderModel {
    private String id;
    private String userId;
    private ArrayList<ProductModel> productList;
    private Timestamp time;
    private String orderState;
    private double total;

    public OrderModel(String id, String userId, ArrayList<ProductModel> productList, Timestamp time, String orderState, double total) {
        this.id = id;
        this.userId = userId;
        this.productList = productList;
        this.time = time;
        this.orderState = orderState;
        this.total = total;
    }

    public ArrayList<ProductModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductModel> productList) {
        this.productList = productList;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
