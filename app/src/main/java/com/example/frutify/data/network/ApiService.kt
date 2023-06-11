package com.example.frutify.data.network

import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.RegisterResponse
import retrofit2.Call
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
        @Field("phone") phone: String
    ) : Call<RegisterResponse>
}