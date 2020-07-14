package com.bs.ecommerce.cart.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class ShippingOption(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DeliveryDateFormat")
    val deliveryDateFormat: Any?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Price")
    val price: String?,
    @SerializedName("Rate")
    val rate: Int?,
    @SerializedName("Selected")
    val selected: Boolean?,
    @SerializedName("ShippingRateComputationMethodSystemName")
    val shippingRateComputationMethodSystemName: String?
)