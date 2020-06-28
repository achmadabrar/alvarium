package com.bs.ecommerce.more.downloadableProducts.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductList
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.*
import com.bs.ecommerce.utils.TextUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownloadableProductListModelImpl : DownloadableProductListModel {

    override fun getProductList(callback: RequestCompleteListener<DownloadableProductListResponse>) {

        RetroClient.api.getDownloadableProducts().enqueue(object : Callback<DownloadableProductListResponse> {

            override fun onFailure(call: Call<DownloadableProductListResponse>, t: Throwable)

                    = callback.onRequestFailed(t.localizedMessage ?: "Unknown")


            override fun onResponse(call: Call<DownloadableProductListResponse>, response: Response<DownloadableProductListResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as DownloadableProductListResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun download(addressId: Int, callback: RequestCompleteListener<Any?>) {

        RetroClient.api.deleteCustomerAddress(addressId).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200)
                    callback.onRequestSuccess(null)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }
}