package com.bs.ecommerce.cart.model

import android.content.Context
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartModelImpl(private val context: Context): CartModel
{

    override fun getCartData(callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.getCartData().enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun updateCartData(keyValuePairs: List<KeyValuePair>, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.updateCartData(keyValuePairs).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }


    override fun applyCouponModel(discount: AddDiscountPostData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyDiscountCoupon(discount).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun applyGiftCardModel(discount: AddDiscountPostData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyGiftCardCoupon(discount).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

}