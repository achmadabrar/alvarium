package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class ReturnReqFormResponse(
    @SerializedName("Data")
    val `data`: ReturnReqFormData?
): BaseResponse()