package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.product.model.data.CustomProperties
import com.google.gson.annotations.SerializedName

data class SpecificationFilter(
    @SerializedName("AlreadyFilteredItems")
    val alreadyFilteredItems: List<Any>? = listOf(),
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Enabled")
    val enabled: Boolean? = false,
    @SerializedName("NotFilteredItems")
    val notFilteredItems: List<Any>? = listOf()
)