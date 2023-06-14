package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName


data class Fruit(

    @field:SerializedName("filename")
    val filename: String? = null,

    @field:SerializedName("fruit")
    val fruit: Any? = null,

    @field:SerializedName("price_estimation")
    val priceEstimation: Int? = null,

    @field:SerializedName("quality")
    val quality: String? = null
)