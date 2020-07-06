package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class TaxRate(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Rate")
    val rate: String?,
    @SerializedName("Value")
    val value: String?
)