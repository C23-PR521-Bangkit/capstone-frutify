package com.example.frutify.data.model

import com.google.gson.annotations.SerializedName

data class ProductListBuyerResponse(

	@field:SerializedName("MESSAGE")
	val MESSAGE: String? = null,

	@field:SerializedName("STATUS")
	val STATUS: String? = null,

	@field:SerializedName("SENDER")
	val SENDER: String? = null,

	@field:SerializedName("PAYLOAD")
	val PAYLOAD: PayloadBuyer? = null
)

data class ProductItemBuyer(

	@field:SerializedName("PRODUCT_FILE_PATH")
	val PRODUCTFILEPATH: String? = null,

	@field:SerializedName("USER_TOKEN")
	val USERTOKEN: String? = null,

	@field:SerializedName("USER_EMAIL")
	val USEREMAIL: String? = null,

	@field:SerializedName("USER_ID")
	val USERID: Int? = null,

	@field:SerializedName("USER_ADDRESS")
	val USERADDRESS: String? = null,

	@field:SerializedName("PRODUCT_ID")
	val PRODUCTID: Int? = null,

	@field:SerializedName("PRODUCT_NAME")
	val PRODUCTNAME: String? = null,

	@field:SerializedName("PRODUCT_QUALITY")
	val PRODUCTQUALITY: String? = null,

	@field:SerializedName("FRUIT_ID")
	val FRUITID: Int? = null,

	@field:SerializedName("PRODUCT_UNIT")
	val PRODUCTUNIT: String? = null,

	@field:SerializedName("PRODUCT_DESCRIPTION")
	val PRODUCTDESCRIPTION: String? = null,

	@field:SerializedName("PRODUCT_PRICE")
	val PRODUCTPRICE: Int? = null,

	@field:SerializedName("USER_PHONE")
	val USERPHONE: String? = null,

	@field:SerializedName("USER_PASSWORD")
	val USERPASSWORD: String? = null,

	@field:SerializedName("FRUIT_NAME")
	val FRUITNAME: String? = null,

	@field:SerializedName("USER_TOKEN_EXPIRED")
	val USERTOKENEXPIRED: String? = null,

	@field:SerializedName("USER_ROLE")
	val USERROLE: String? = null,

	@field:SerializedName("USER_FULLNAME")
	val USERFULLNAME: String? = null
)

data class PayloadBuyer(

	@field:SerializedName("product")
	val product: List<ProductItemBuyer?>? = null
)
