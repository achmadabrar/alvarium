package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

data class ProductByVendorResponse(
    @SerializedName("Data")
    val data: ProductByVendorData?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)