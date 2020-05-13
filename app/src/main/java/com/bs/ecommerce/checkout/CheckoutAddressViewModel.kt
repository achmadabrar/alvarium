package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.networking.common.Data
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData

open class CheckoutAddressViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartRootData>()

    var getBillingAddressLD = MutableLiveData<BillingAddressResponse>()

    var stateListLD = MutableLiveData<List<AvailableState>>()

    var saveResponseLD = MutableLiveData<CheckoutSaveResponse>()

    fun getBillingFormVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getBillingForm(object : RequestCompleteListener<BillingAddressResponse>
        {
            override fun onRequestSuccess(data: BillingAddressResponse)
            {
                isLoadingLD.postValue(false)

                getBillingAddressLD.postValue(data)
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

        model.saveNewBilling(saveBillingPostData, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

                saveResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun saveExistingBillingVM(addressId: Long, model: CheckoutModel, shipToSameAddress: Boolean)
    {

        isLoadingLD.postValue(true)

        val existingAddress = ExistingAddress(
            formValues = listOf(getKeyValue("billing_address_id", addressId)),
            data = Data(shipToSameAddress = shipToSameAddress))

        model.saveExistingBilling(existingAddress, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

                saveResponseLD.postValue(data)
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

        model.saveNewShipping(saveShippingPostData, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

                saveResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    private fun getKeyValue(key: String, addressId: Long) = KeyValuePair(key = key, value = addressId.toString())

    fun saveShippingFromExistingAddressVM(type: Int, addressId: Long, model: CheckoutModel)
    {
        var existingAddress = ExistingAddress()

        when(type)
        {
            Constants.BillingAddress -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue("billing_address_id", addressId)),
                    data = Data(shipToSameAddress = false))
            }

            Constants.ShippingAddress -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue("shipping_address_id", addressId)),
                    data = Data(shipToSameAddress = false))
            }

            Constants.StorePickUp -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue("pickup-points-id", addressId)),
                    data = Data(pickupInStore = false)
                )
            }

        }

        isLoadingLD.postValue(true)


        model.saveExistingShipping(existingAddress, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

                saveResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }
        })
    }



    fun saveShippingMethodVM(value: String, model: CheckoutModel)
    {

        val keyValuePair = KeyValuePair()
        keyValuePair.key = "shippingoption"
        keyValuePair.value = value

        isLoadingLD.postValue(true)

        model.saveShippingMethod(KeyValueFormData(listOf(keyValuePair)), object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

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

        model.getCheckoutConfirmInformation(object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                isLoadingLD.postValue(false)

            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

}