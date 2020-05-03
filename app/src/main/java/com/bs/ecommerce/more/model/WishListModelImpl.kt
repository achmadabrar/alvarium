package com.bs.ecommerce.more.model


import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.WishListResponse
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

        RetroClient.api.updateWishList(keyValuePairs).enqueue(object : Callback<WishListResponse> {

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
        RetroClient.api.moveWishListItemsToCart(keyValuePairs).enqueue(object : Callback<WishListResponse> {

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