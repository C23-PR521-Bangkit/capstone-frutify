package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("MESSAGE")
	val MESSAGE: String? = null,

	@field:SerializedName("STATUS")
	val STATUS: String? = null,

	@field:SerializedName("SENDER")
	val SENDER: String? = null,

	@field:SerializedName("PAYLOAD")
	val PAYLOAD: PAYLOAD? = null
)

data class PAYLOAD(

	@field:SerializedName("user")
	val users: Users? = null
)
