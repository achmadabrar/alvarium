package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.product.model.data.OrderDetailsResponse
import com.bs.ecommerce.product.model.data.OrderHistoryData
import com.bs.ecommerce.product.model.data.OrderHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderModelImpl : OrderModel {

    override fun getOrderHistory(callback: RequestCompleteListener<OrderHistoryData>) {
        RetroClient.api.getOrderHistory().enqueue(object :
            Callback<OrderHistoryResponse> {
            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<OrderHistoryResponse>, response: Response<OrderHistoryResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.orderHistory as OrderHistoryData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun getOrderDetails(
        orderId: Int,
        callback: RequestCompleteListener<OrderDetailsData>
    ) {

        RetroClient.api.getOrderDetails(orderId).enqueue(object :
            Callback<OrderDetailsResponse> {

            override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<OrderDetailsResponse>,
                response: Response<OrderDetailsResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as OrderDetailsData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }
}