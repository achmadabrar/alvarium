package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class ProductBreadcrumb(
    @SerializedName("CategoryBreadcrumb")
    val categoryBreadcrumb: List<Any>?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Enabled")
    val enabled: Boolean?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?
)