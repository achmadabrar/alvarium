package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("Data")
    val `data`: OrderDetailsData?
) : BaseResponse()