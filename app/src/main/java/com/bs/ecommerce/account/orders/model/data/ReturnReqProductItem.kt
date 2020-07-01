package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class ReturnReqProductItem(
    @SerializedName("AttributeInfo")
    val attributeInfo: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("Quantity")
    val quantity: Int?,
    @SerializedName("UnitPrice")
    val unitPrice: String?,
    var customerInput: Int = 0
)