package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class Login(

	@field:SerializedName("loginResult")
	val loginResult: User? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


