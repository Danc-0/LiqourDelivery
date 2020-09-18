package com.danc.winesapi.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;

    @SerializedName("title")
    private String productName;

    @SerializedName("description")
    private String productDescription;

    @SerializedName("current_price")
    private String productPrice;

    @SerializedName("old_price")
    private String initialPrice;

    @SerializedName("size")
    private String quantity;

    private String ratings;

    @SerializedName("num_ratings")
    private String ratingsNumbers;

    @SerializedName("image")
    private String imageUrl;

    public Product(){}

    protected Product(Parcel in) {
        id = in.readString();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readString();
        initialPrice = in.readString();
        quantity = in.readString();
        ratings = in.readString();
        ratingsNumbers = in.readString();
        imageUrl = in.readString();
    }

//    public static final Creator<Product> CREATOR = new Creator<Product>() {
//        @Override
//        public Product createFromParcel(Parcel in) {
//            return new Product(in);
//        }
//
//        @Override
//        public Product[] newArray(int size) {
//            return new Product[size];
//        }
//    };

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                "productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", initialPrice='" + initialPrice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", ratings='" + ratings + '\'' +
                ", ratingsNumbers='" + ratingsNumbers + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(String initialPrice) {
        this.initialPrice = initialPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRatingsNumbers() {
        return ratingsNumbers;
    }

    public void setRatingsNumbers(String ratingsNumbers) {
        this.ratingsNumbers = ratingsNumbers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    protected Product(Parcel in) {
//        productName = in.readString();
//        productDescription = in.readString();
//        imageUrl = String.valueOf(in.readInt());
//        productPrice = String.valueOf(in.readInt());
//        initialPrice = String.valueOf(in.readInt());
//        ratingsNumbers = String.valueOf(in.readInt());
//        ratings = String.valueOf(in.readInt());
//        serial_number = String.valueOf(in.readInt());
//        quantity = Integer.parseInt(in.readString());
//    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int i) {
//        dest.writeString(productName);
//        dest.writeString(productDescription);
//        dest.writeString(imageUrl);
//        dest.writeString(productPrice);
//        dest.writeString(initialPrice);
//        dest.writeString(ratingsNumbers);
//        dest.writeString(ratings);
////        dest.writeString(serial_number);
//    }

    public Product(String id, String productName, String productDescription, String productPrice, String initialPrice,
                   String quantity, String ratings, String ratingsNumbers, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.initialPrice = initialPrice;
        this.quantity = quantity;
        this.ratings = ratings;
        this.ratingsNumbers = ratingsNumbers;
        this.imageUrl = imageUrl;
    }
}
