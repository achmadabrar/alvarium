package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("Data")
    val `data`: ProductResponseData? = ProductResponseData(),
    @SerializedName("ErrorList")
    val errorList: List<Any>? = listOf()
)