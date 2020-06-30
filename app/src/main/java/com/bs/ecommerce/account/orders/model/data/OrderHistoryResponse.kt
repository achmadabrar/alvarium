package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.account.orders.model.data.OrderHistoryData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

class OrderHistoryResponse(
    @SerializedName("Data")
    val orderHistory: OrderHistoryData?
) : BaseResponse()