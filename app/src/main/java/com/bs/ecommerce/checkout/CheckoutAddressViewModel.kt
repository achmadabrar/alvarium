package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.networking.common.KeyValueFormData

open class CheckoutAddressViewModel : BaseViewModel()
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


    fun saveNewBillingVM(newAddress: AddressModel, model: CheckoutModel)
    {
        val saveBillingPostData = SaveBillingPostData()
        saveBillingPostData.data = BillingAddress()
        saveBillingPostData.data.billingNewAddress = newAddress

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

    fun saveNewShippingVM(newAddress: AddressModel, model: CheckoutModel)
    {
        val saveShippingPostData = SaveShippingPostData()
        saveShippingPostData.data = ShippingAddress()
        saveShippingPostData.data.shippingNewAddress = newAddress

        isLoadingLD.postValue(true)

        model.saveNewShipping(saveShippingPostData, object : RequestCompleteListener<SaveBillingResponse>
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

    fun saveShippingFromExistingAddressVM(type: Int, addressId: Long, model: CheckoutModel)
    {
        var key = ""

        when(type)
        {
            Constants.BillingAddress -> key = "billing_address_id"
            Constants.ShippingAddress -> key = "shipping_address_id"
            Constants.StorePickUp -> key = "pickup_points_id"
        }


        isLoadingLD.postValue(true)

        val keyValuePair = KeyValuePair()
        keyValuePair.key = key
        keyValuePair.value = addressId.toString()

        model.saveExistingShipping(KeyValueFormData(listOf(keyValuePair)), object : RequestCompleteListener<SaveBillingResponse>
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