package com.danc.winesapi.Models;

import com.google.gson.annotations.SerializedName;

public class OrderProducts {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("count")
    private String productCount;

    public OrderProducts(String productId, String productCount) {
        this.productId = productId;
        this.productCount = productCount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

}
