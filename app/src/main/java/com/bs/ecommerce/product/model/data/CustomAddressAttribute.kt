package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class CustomAddressAttribute(
    @SerializedName("AttributeControlType")
    val attributeControlType: Int?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DefaultValue")
    val defaultValue: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsRequired")
    val isRequired: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Values")
    val values: List<AddressAttributeValue>?
)