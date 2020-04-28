package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class TaxRate(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Rate")
    val rate: String?,
    @SerializedName("Value")
    val value: String?
)