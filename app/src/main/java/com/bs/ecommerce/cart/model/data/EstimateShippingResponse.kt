package com.bs.ecommerce.cart.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class EstimateShippingResponse(
    @SerializedName("Data")
    val `data`: EstimateShippingData?
): BaseResponse()