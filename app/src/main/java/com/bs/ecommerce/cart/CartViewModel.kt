package com.bs.ecommerce.cart

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData

class CartViewModel : BaseViewModel()
{

    private var dynamicAttributeUpdated = false

    fun updateCartData(allKeyValueList: List<KeyValuePair>, model: CartModel)
    {

        val body = KeyValueFormData(
            allKeyValueList
        )
        body.formValues = allKeyValueList

        isLoadingLD.value = true

        model.updateCartData(body, object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
    }

    fun calculateCostWithUpdatedAttributes(keyValueFormData: KeyValueFormData, model: CartModel) {
        // if(!dynamicAttributeUpdated) return

        isLoadingLD.value = true

        model.applyCheckoutAttributes(keyValueFormData.formValues, object : RequestCompleteListener<CartRootData>
        {
            override fun onRequestSuccess(data: CartRootData)
            {
                isLoadingLD.value = false
                dynamicAttributeUpdated = false

                cartLD.value = data
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
                dynamicAttributeUpdated = false
            }
        })
    }

    fun applyCouponVM(discount : AddDiscountPostData, model: CartModel)
    {

        isLoadingLD.value = true

        model.applyCouponModel(discount, object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
    }

    fun applyGiftCardVM(discount : AddDiscountPostData, model: CartModel)
    {

        isLoadingLD.value = true

        model.applyGiftCardModel(discount, object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
    }
}