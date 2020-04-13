package com.bs.ecommerce.product

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.data.ProductResponse
import com.bs.ecommerce.product.data.ProductResponseData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListModelImpl : ProductListModel {

    override fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductResponseData>) {

        RetroClient.api.getProductList(catId, queryMap).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as ProductResponseData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun fetchProductsByManufacturer(
        manufacturerId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductResponseData>
    ) {
        RetroClient.api.getProductListByManufacturer(manufacturerId, queryMap).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as ProductResponseData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }


}