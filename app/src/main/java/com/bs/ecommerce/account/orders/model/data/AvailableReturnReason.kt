package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class AvailableReturnReason(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Name")
    val name: String?
) {
    override fun toString(): String {
        return name ?: ""
    }
}