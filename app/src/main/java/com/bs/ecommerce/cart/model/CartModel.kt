package com.bs.ecommerce.cart.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData

interface CartModel
{
    fun getCartData(callback: RequestCompleteListener<CartResponse>)

    fun updateCartData(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun removeCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyCheckoutAttributes(list: List<KeyValuePair>, callback: RequestCompleteListener<CartRootData>)
}