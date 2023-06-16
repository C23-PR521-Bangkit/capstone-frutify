package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("MESSAGE")
	val MESSAGE: String? = null,

	@field:SerializedName("STATUS")
	val STATUS: String? = null,

	@field:SerializedName("SENDER")
	val SENDER: String? = null,

	@field:SerializedName("PAYLOAD")
	val PAYLOAD: PayloadPredict? = null
)

data class PayloadPredict(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("fruit")
	val fruit: String? = null,

	@field:SerializedName("classes")
	val classes: List<Any?>? = null,

	@field:SerializedName("price_estimation")
	val priceEstimation: Int? = null,

	@field:SerializedName("precentage")
	val precentage: Float? = null,

	@field:SerializedName("quality")
	val quality: String? = null
)
