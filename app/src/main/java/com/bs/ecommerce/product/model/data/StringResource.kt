package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class StringResource(
    @SerializedName("Key")
    val key: String,
    @SerializedName("Value")
    val value: String
)