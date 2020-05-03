package com.bs.ecommerce.cart.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData

interface CartModel
{
    fun getCartData(callback: RequestCompleteListener<CartResponse>)

    fun updateCartData(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyCouponModel(discount: AddDiscountPostData, callback: RequestCompleteListener<CartResponse>)

    fun applyGiftCardModel(discount: AddDiscountPostData, callback: RequestCompleteListener<CartResponse>)

    fun applyCheckoutAttributes(list: List<KeyValuePair>, callback: RequestCompleteListener<CartRootData>)
}