package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class OrderHistoryData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Orders")
    val orders: List<Order>?,
    @SerializedName("RecurringOrders")
    val recurringOrders: List<Any>?,
    @SerializedName("RecurringPaymentErrors")
    val recurringPaymentErrors: List<Any>?
)