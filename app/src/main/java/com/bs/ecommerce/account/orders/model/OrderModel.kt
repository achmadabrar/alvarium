package com.bs.ecommerce.account.orders.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.account.orders.model.data.OrderDetailsData
import com.bs.ecommerce.account.orders.model.data.OrderHistoryData
import com.bs.ecommerce.account.orders.model.data.ShipmentDetailsData
import okhttp3.ResponseBody
import retrofit2.Response

interface OrderModel {
    fun getOrderHistory(
        callback: RequestCompleteListener<OrderHistoryData>
    )

    fun getOrderDetails(
        orderId: Int,
        callback: RequestCompleteListener<OrderDetailsData>
    )
    fun getShipmentDetails(
        shipmentId: Int,
        callback: RequestCompleteListener<ShipmentDetailsData>
    )


    fun reorder(
        orderId: Int,
        callback: RequestCompleteListener<Boolean>
    )

    fun downloadPdfInvoice(
        orderId: Int,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )

    fun downloadOrderNotes(
        notesId: Int,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )
}