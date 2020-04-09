package com.bs.ecommerce.cart.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.common.RequestCompleteListener

interface CartModel
{
    fun getCartData(callback: RequestCompleteListener<CartResponse>)

    fun updateCartData(list: List<KeyValuePair>, callback: RequestCompleteListener<CartResponse>)

}