package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class VendorModel(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("SeName")
    val seName: String?
)