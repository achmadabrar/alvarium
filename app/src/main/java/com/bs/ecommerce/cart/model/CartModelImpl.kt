package com.bs.ecommerce.cart.model

import android.content.Context
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.UpdateCartPostData
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

    override fun updateCartData(updateCartPostData: UpdateCartPostData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.updateCartData(updateCartPostData).enqueue(object : Callback<CartResponse>
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

    override fun applyCheckoutAttributes(
        list: List<KeyValuePair>,
        callback: RequestCompleteListener<CartRootData>
    ) {
        RetroClient.api.updateCheckOutAttribute(list).enqueue(object : Callback<CartRootData> {
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