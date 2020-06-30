package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class ProductManufacturers(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsActive")
    val isActive: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("SeName")
    val seName: String?
)