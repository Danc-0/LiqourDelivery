package com.danc.winesapi.Models;

import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    @SerializedName("email")
    private String Email;

    @SerializedName("username")
    private String UserName;

    @SerializedName("name")
    private String Name;

    @SerializedName("phone")
    private String PhoneNumber;

    @SerializedName("password")
    private String Password;

    @Override
    public String toString() {
        return "UsersAuth{" +
                "Email='" + Email + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Name='" + Name + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public RegisterUser(String email, String userName, String name, String phoneNumber, String password) {
        Email = email;
        UserName = userName;
        Name = name;
        PhoneNumber = phoneNumber;
        Password = password;
    }
}
