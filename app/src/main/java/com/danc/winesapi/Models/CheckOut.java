package com.danc.winesapi.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckOut {

    @SerializedName("user")
    private String email;

    @SerializedName("products")
    private List<OrderProducts> products;

    public CheckOut(String email, List<OrderProducts> products) {
        this.email = email;
        this.products = products;
    }
}
