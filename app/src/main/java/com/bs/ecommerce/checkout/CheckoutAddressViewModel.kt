package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.AvailableState
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.checkout.model.data.StateListResponse
import com.bs.ecommerce.common.RequestCompleteListener

class CheckoutAddressViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartRootData>()

    var billingAddressResponseLD = MutableLiveData<BillingAddressResponse>()

    var stateListLD = MutableLiveData<List<AvailableState>>()

    fun getBillingFormVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getBillingForm(object : RequestCompleteListener<BillingAddressResponse>
        {
            override fun onRequestSuccess(data: BillingAddressResponse)
            {
                isLoadingLD.postValue(false)

                billingAddressResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun getStatesByCountryVM(countryCode: String, model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getStatesByCountryForm(countryCode, object : RequestCompleteListener<StateListResponse>
        {
            override fun onRequestSuccess(data: StateListResponse)
            {
                isLoadingLD.postValue(false)
                stateListLD.postValue(data.stateList)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun getShippingMethodVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getShippingMethod(object : RequestCompleteListener<CartResponse>
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

    fun getPaymentMethodVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getPaymentMethod(object : RequestCompleteListener<CartResponse>
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

    fun getCheckoutConfirmInformationVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getCheckoutConfirmInformation(object : RequestCompleteListener<CartResponse>
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