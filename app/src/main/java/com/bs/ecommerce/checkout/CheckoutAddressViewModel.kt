package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData

class CheckoutAddressViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartRootData>()

    var billingAddressResponseLD = MutableLiveData<BillingAddressResponse>()

    var stateListLD = MutableLiveData<List<AvailableState>>()

    var saveBillingResponseLD = MutableLiveData<SaveBillingResponse>()

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
    fun saveNewBillingVM(billingAddress: BillingAddress, model: CheckoutModel)
    {
        val saveBillingPostData = SaveBillingPostData()
        saveBillingPostData.data = billingAddress

        isLoadingLD.postValue(true)

        model.saveNewBilling(saveBillingPostData, object : RequestCompleteListener<SaveBillingResponse>
        {
            override fun onRequestSuccess(data: SaveBillingResponse)
            {
                isLoadingLD.postValue(false)

                saveBillingResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun saveBillingFromExistingAddressVM(addressId: Long, model: CheckoutModel)
    {

        val key = "billing_address_id"

        isLoadingLD.postValue(true)

        val keyValuePair = KeyValuePair()
        keyValuePair.key = key
        keyValuePair.value = addressId.toString()

        model.saveExistingBilling(KeyValueFormData(listOf(keyValuePair)), object : RequestCompleteListener<SaveBillingResponse>
        {
            override fun onRequestSuccess(data: SaveBillingResponse)
            {
                isLoadingLD.postValue(false)

                saveBillingResponseLD.postValue(data)
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