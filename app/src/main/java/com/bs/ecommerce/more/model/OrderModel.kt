package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.product.model.data.OrderHistoryData

interface OrderModel {
    fun getOrderHistory(
        callback: RequestCompleteListener<OrderHistoryData>
    )

    fun getOrderDetails(
        orderId: Int,
        callback: RequestCompleteListener<OrderDetailsData>
    )

    fun reorder(
        orderId: Int,
        callback: RequestCompleteListener<Boolean>
    )
}