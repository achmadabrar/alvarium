package com.bs.ecommerce.catalog.common

import com.bs.ecommerce.catalog.common.ProductByVendorData
import com.google.gson.annotations.SerializedName

data class ProductByVendorResponse(
    @SerializedName("Data")
    val data: ProductByVendorData?,
    @SerializedName("ErrorList")
    val errorList: List<String>?,
    @SerializedName("Message")
    val message: Any?
)