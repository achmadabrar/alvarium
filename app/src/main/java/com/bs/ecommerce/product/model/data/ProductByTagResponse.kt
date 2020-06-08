package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

data class ProductByTagResponse(
    @SerializedName("Data")
    val data: ProductByTagData?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)