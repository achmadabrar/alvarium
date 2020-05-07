package com.bs.ecommerce.checkout.model


import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData

interface CheckoutModel
{
    fun getBillingForm(callback: RequestCompleteListener<BillingAddressResponse>)

    fun getStatesByCountryForm(countryCode: String, callback: RequestCompleteListener<StateListResponse>)

    fun saveNewBilling(billingAddress: SaveBillingPostData, callback: RequestCompleteListener<SaveBillingResponse>)

    fun saveExistingBilling(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<SaveBillingResponse>)

    fun saveNewShipping(shippingAddress: SaveShippingPostData, callback: RequestCompleteListener<SaveBillingResponse>)

    fun saveExistingShipping(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<SaveBillingResponse>)

    fun getShippingMethod(callback: RequestCompleteListener<CartResponse>)

    fun saveShippingMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)

    fun getPaymentMethod(callback: RequestCompleteListener<CartResponse>)

    fun savePaymentMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)

    fun getCheckoutConfirmInformation(callback: RequestCompleteListener<CartResponse>)

}