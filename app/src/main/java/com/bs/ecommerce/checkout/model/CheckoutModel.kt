package com.bs.ecommerce.checkout.model


import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.checkout.model.data.CheckoutPostData
import com.bs.ecommerce.checkout.model.data.StateListResponse
import com.bs.ecommerce.common.RequestCompleteListener

interface CheckoutModel
{
    fun getBillingForm(callback: RequestCompleteListener<BillingAddressResponse>)

    fun getStatesByCountryForm(countryCode: String, callback: RequestCompleteListener<StateListResponse>)

    fun getShippingMethod(callback: RequestCompleteListener<CartResponse>)

    fun saveShippingMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)

    fun getPaymentMethod(callback: RequestCompleteListener<CartResponse>)

    fun savePaymentMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)

    fun getCheckoutConfirmInformation(callback: RequestCompleteListener<CartResponse>)

}