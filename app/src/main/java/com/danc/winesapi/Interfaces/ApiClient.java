package com.danc.winesapi.Interfaces;

import com.danc.winesapi.Models.LoginUser;
import com.danc.winesapi.Models.Product;
import com.danc.winesapi.Models.RegisterUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiClient {

    @FormUrlEncoded
    @POST("api/user/register/")
    Call<Void> createNewUser(
            @Field("email") String email,
            @Field("username") String username,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password

    );

    @POST("api/user/login/")
    Call<Void> loginNewUser (
            @Field("email") String email,
            @Field("password") String password

    );

    @GET("api/user/register/")
    Call<LoginUser> userLogin(@Body LoginUser loginUser);

    @POST("api/user/register/")
    Call<RegisterUser> userRegistration (@Body RegisterUser registerUser);

    @GET("api/products/")
    Call<List<Product>> getProducts();

}