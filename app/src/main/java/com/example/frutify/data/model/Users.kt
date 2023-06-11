package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class Users(

    @field:SerializedName("USER_PHONE")
    val USERPHONE: String? = null,

    @field:SerializedName("USER_PASSWORD")
    val USERPASSWORD: String? = null,

    @field:SerializedName("USER_TOKEN")
    val USERTOKEN: String? = null,

    @field:SerializedName("USER_EMAIL")
    val USEREMAIL: String? = null,

    @field:SerializedName("USER_ID")
    val USERID: Int? = null,

    @field:SerializedName("USER_TOKEN_EXPIRED")
    val USERTOKENEXPIRED: String? = null,

    @field:SerializedName("USER_ADDRESS")
    val USERADDRESS: String? = null,

    @field:SerializedName("USER_FULLNAME")
    val USERFULLNAME: String? = null
)