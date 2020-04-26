package com.bs.ecommerce.cart

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener

class CartViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartRootData>()


    fun getCartVM(model: CartModel)
    {

        isLoadingLD.postValue(true)

        model.getCartData(object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.postValue(false)

                cartLD.postValue(data.cartRootData)
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

                cartLD.postValue(data.cartRootData)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun applyCouponVM(discount : AddDiscountPostData, model: CartModel)
    {

        isLoadingLD.postValue(true)

        model.applyCouponModel(discount, object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.postValue(false)

                cartLD.postValue(data.cartRootData)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }
}