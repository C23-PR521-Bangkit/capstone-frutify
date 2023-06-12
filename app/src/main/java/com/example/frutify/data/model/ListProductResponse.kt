package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class ListProductResponse(

	@field:SerializedName("MESSAGE")
	val MESSAGE: String? = null,

	@field:SerializedName("STATUS")
	val STATUS: String? = null,

	@field:SerializedName("SENDER")
	val SENDER: String? = null,

	@field:SerializedName("PAYLOAD")
	val PAYLOAD: ProductPayload? = null
)

data class ProductPayload(

	@field:SerializedName("product")
	val product: List<ProductItem?>? = null
)
