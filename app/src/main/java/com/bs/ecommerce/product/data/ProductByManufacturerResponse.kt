package com.bs.ecommerce.product.data

import com.google.gson.annotations.SerializedName

data class ProductByManufacturerResponse(
    @SerializedName("Data")
    val manufacturer: Manufacturer?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)