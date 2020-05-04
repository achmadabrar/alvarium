package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class AddressAttributeValue(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsPreSelected")
    val isPreSelected: Boolean?,
    @SerializedName("Name")
    val name: String?
)