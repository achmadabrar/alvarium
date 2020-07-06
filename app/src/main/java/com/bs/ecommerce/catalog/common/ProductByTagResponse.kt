package com.bs.ecommerce.catalog.common

import com.bs.ecommerce.catalog.common.ProductByTagData
import com.google.gson.annotations.SerializedName

data class ProductByTagResponse(
    @SerializedName("Data")
    val data: ProductByTagData?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)