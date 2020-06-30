package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("Data")
    val `data`: OrderDetailsData?
) : BaseResponse()