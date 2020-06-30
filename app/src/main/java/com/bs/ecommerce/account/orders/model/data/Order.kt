package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.product.model.data.CustomProperties
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("CreatedOn")
    val createdOn: String?,
    @SerializedName("CustomOrderNumber")
    val customOrderNumber: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsReturnRequestAllowed")
    val isReturnRequestAllowed: Boolean?,
    @SerializedName("OrderStatus")
    val orderStatus: String?,
    @SerializedName("OrderStatusEnum")
    val orderStatusEnum: Int?,
    @SerializedName("OrderTotal")
    val orderTotal: String?,
    @SerializedName("PaymentStatus")
    val paymentStatus: String?,
    @SerializedName("ShippingStatus")
    val shippingStatus: String?
)