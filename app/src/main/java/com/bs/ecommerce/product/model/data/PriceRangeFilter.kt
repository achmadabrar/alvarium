package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.product.model.data.CustomProperties
import com.google.gson.annotations.SerializedName

data class PriceRangeFilter(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Enabled")
    val enabled: Boolean? = false,
    @SerializedName("Items")
    val items: List<Any>? = listOf()
)