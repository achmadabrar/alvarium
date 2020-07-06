package com.bs.ecommerce.checkout.model


import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData

interface CheckoutModel
{
    fun getBillingForm(callback: RequestCompleteListener<BillingAddressResponse>)

    fun getStatesByCountryForm(countryCode: String, callback: RequestCompleteListener<StateListResponse>)

    fun saveNewBilling(billingAddress: SaveBillingPostData, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun saveExistingBilling(postModel: ExistingAddress, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun saveNewShipping(shippingAddress: SaveShippingPostData, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun saveExistingShipping(postModel: ExistingAddress, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun saveShippingMethod(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun savePaymentMethod(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun getCheckoutConfirmInformation(callback: RequestCompleteListener<ConfirmOrderResponse>)

    fun submitConfirmOrder(callback: RequestCompleteListener<CheckoutSaveResponse>)

    fun completeOrder(callback: RequestCompleteListener<CheckoutSaveResponse>)

}