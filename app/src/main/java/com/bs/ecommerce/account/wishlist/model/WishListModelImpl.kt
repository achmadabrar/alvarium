package com.bs.ecommerce.account.wishlist.model


import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.networking.common.RequestCompleteListener

import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.account.wishlist.model.data.WishListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListModelImpl : WishListModel {

    override fun getWishList(callback: RequestCompleteListener<WishListResponse>) {

        RetroClient.api.getWishList().enqueue(object : Callback<WishListResponse> {
            override fun onResponse(call: Call<WishListResponse>, response: Response<WishListResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as WishListResponse)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<WishListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun updateWishListData(
        keyValuePairs: List<KeyValuePair>,
        callback: RequestCompleteListener<WishListResponse>
    ) {

        RetroClient.api.updateWishList(KeyValueFormData(keyValuePairs)).enqueue(object : Callback<WishListResponse> {

            override fun onResponse(call: Call<WishListResponse>, response: Response<WishListResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as WishListResponse)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WishListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun moveItemsToCart(
        keyValuePairs: List<KeyValuePair>,
        callback: RequestCompleteListener<WishListResponse>
    ) {
        val reqBody = BaseResponse().apply { formValues = keyValuePairs }

        RetroClient.api.moveWishListItemsToCart(reqBody).enqueue(object : Callback<WishListResponse> {

            override fun onResponse(call: Call<WishListResponse>, response: Response<WishListResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as WishListResponse)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WishListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }
}