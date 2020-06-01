package com.bs.ecommerce.cart.model

import android.content.Context
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.KeyValueFormData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartModelImpl: CartModel
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
    override fun updateCartData(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.updateCartData(keyValueFormData).enqueue(object : Callback<CartResponse>
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


    override fun applyCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyDiscountCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
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

    override fun removeCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {
        RetroClient.api.removeDiscountCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
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

    override fun applyGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyGiftCardCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
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
    override fun removeGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {
        RetroClient.api.removeGiftCardCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
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

    override fun applyCheckoutAttributes(
        list: List<KeyValuePair>,
        callback: RequestCompleteListener<CartRootData>
    ) {
        RetroClient.api.updateCheckOutAttribute(KeyValueFormData(list)).enqueue(object : Callback<CartRootData> {
            override fun onResponse(call: Call<CartRootData>, response: Response<CartRootData>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartRootData)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartRootData>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }


}