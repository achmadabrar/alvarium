package com.bs.ecommerce.product.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

class OrderHistoryResponse(
    @SerializedName("Data")
    val orderHistory: OrderHistoryData?
) : BaseResponse()