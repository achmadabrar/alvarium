package com.bs.ecommerce.product.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.ProductDetailModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ProductDetailModelImpl :
    ProductDetailModel {

    override fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>) {

        RetroClient.api.getProductDetails(productId).enqueue(object : Callback<ProductDetailResponse> {
            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ProductDetailResponse>, response: Response<ProductDetailResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as ProductDetailResponse)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun addProductToCartModel(keyValueList: ArrayList<KeyValuePair>, callback: RequestCompleteListener<ProductDetailResponse>){

        /*RetroClient.api.getProductDetails(productId).enqueue(object : Callback<ProductDetailResponse> {
            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ProductDetailResponse>, response: Response<ProductDetailResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as ProductDetailResponse)
                else
                    callback.onRequestFailed(response.message())
            }

        })*/
    }

}