package com.example.frutify.data.network

import com.example.frutify.data.model.ListProductResponse
import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("role") role: String
    ) : Call<RegisterResponse>

    @POST("auth/update")
    @FormUrlEncoded
    fun updateUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("fullname") fullname: String,
        @Field("address") address: String,
        @Field("new_password") newPassword: String? = null
    ) : Call<RegisterResponse>

    @POST("product/list")
    @FormUrlEncoded
    fun getListProduct(
        @Field("search") search: String? = null,
        @Field("user_id") userId: Int
    ) : Call<ListProductResponse>

    @POST("product/manage/create")
    @FormUrlEncoded
    fun addProduct(
        @Field("fruit_id") fruitId: Int,
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("filename") filename: String,
        @Field("quality") quality: String
    ) : Call<RegisterResponse>

}