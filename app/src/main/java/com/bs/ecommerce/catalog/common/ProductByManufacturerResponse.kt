package com.bs.ecommerce.catalog.common

import com.bs.ecommerce.home.homepage.model.data.Manufacturer
import com.google.gson.annotations.SerializedName

data class ProductByManufacturerResponse(
    @SerializedName("Data")
    val manufacturer: Manufacturer?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)