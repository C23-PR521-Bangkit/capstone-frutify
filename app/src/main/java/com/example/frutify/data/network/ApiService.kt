package com.example.frutify.data.network

import com.example.frutify.data.model.ImageClasifyResponse
import com.example.frutify.data.model.ListProductResponse
import com.example.frutify.data.model.LoginResponse
import com.example.frutify.data.model.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

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

    @POST("product/manage/update")
    @FormUrlEncoded
    fun updateProduct(
        @Field("product_id") productId: Int,
        @Field("fruit_id") fruitId: Int,
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("filename") filename: String,
        @Field("quality") quality: String
    ) : Call<RegisterResponse>

    @POST("product/manage/delete")
    @FormUrlEncoded
    fun deleteProduct(
        @Field("product_id") productId: Int,
        @Field("user_id") userId: Int
    ) : Call<RegisterResponse>

    @Multipart
    @POST("fruit/classify")
    fun doImageClasify(
        @Part image: MultipartBody.Part
    ): Call<ImageClasifyResponse>
}