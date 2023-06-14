package com.example.frutify.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(

    @field:SerializedName("PRODUCT_DESCRIPTION")
    val PRODUCTDESCRIPTION: String? = null,

    @field:SerializedName("PRODUCT_PRICE")
    val PRODUCTPRICE: Int? = null,

    @field:SerializedName("PRODUCT_FILE_PATH")
    val PRODUCTFILEPATH: String? = null,

    @field:SerializedName("USER_ID")
    val USERID: Int? = null,

    @field:SerializedName("PRODUCT_ID")
    val PRODUCTID: Int? = null,

    @field:SerializedName("PRODUCT_NAME")
    val PRODUCTNAME: String? = null,

    @field:SerializedName("PRODUCT_QUALITY")
    val PRODUCTQUALITY: String? = null,

    @field:SerializedName("FRUIT_ID")
    val FRUITID: Int? = null,

    @field:SerializedName("PRODUCT_UNIT")
    val PRODUCTUNIT: String? = null
) : Parcelable