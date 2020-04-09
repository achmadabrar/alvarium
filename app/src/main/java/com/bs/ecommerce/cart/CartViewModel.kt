package com.bs.ecommerce.cart

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.Category

class CartViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartData>()


    fun getCartData(model: CartModel)
    {

        isLoadingLD.postValue(true)

        model.getCartData(object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.postValue(false)

                cartLD.postValue(data.data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun updateCartData(keyValuePairs: List<KeyValuePair>, model: CartModel)
    {

        isLoadingLD.postValue(true)

        model.updateCartData(keyValuePairs, object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.postValue(false)

                cartLD.postValue(data.data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }
}