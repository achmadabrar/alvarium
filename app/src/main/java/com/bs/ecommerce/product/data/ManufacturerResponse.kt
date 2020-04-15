package com.bs.ecommerce.product.data

import com.google.gson.annotations.SerializedName

data class ManufacturerResponse (
    @SerializedName("Data")
    val manufacturerList: List<Manufacturer>? = listOf(),
    @SerializedName("ErrorList")
    val errorList: List<String>? = listOf(),
    @SerializedName("Message")
    val message: Any?
)