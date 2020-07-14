package com.bs.ecommerce.cart.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class EstimateShippingData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("ShippingOptions")
    val shippingOptions: List<ShippingOption>?,
    @SerializedName("Warnings")
    val warnings: List<String>?
)