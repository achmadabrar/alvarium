package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class ProductTag(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("ProductCount")
    val productCount: Int?,
    @SerializedName("SeName")
    val seName: String?
)