package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.Data
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData

open class CheckoutViewModel : BaseViewModel()
{

    var getBillingAddressLD = MutableLiveData<BillingAddressResponse>()

    var stateListLD = MutableLiveData<List<AvailableState>>()

    var saveResponseLD = MutableLiveData<CheckoutSaveResponse>()

    var getConfirmOrderLD = MutableLiveData<ConfirmOrderResponse>()


    private fun saveCheckoutData(data: CheckoutSaveResponse)
    {
        isLoadingLD.postValue(false)
        saveResponseLD.postValue(data)
    }

    private fun hideLoader_ShowErrorMsg(errorMessage : String)
    {
        isLoadingLD.postValue(false)
        toast(errorMessage)
    }

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

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
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

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }


    fun saveNewBillingVM(
        newAddress: AddressModel,
        formValues: KeyValueFormData,
        model: CheckoutModel
    )
    {
        // remove custom attribute values from requestBody to avoid 500
        newAddress.customAddressAttributes = listOf()

        val saveBillingPostData = SaveBillingPostData()
        saveBillingPostData.data = BillingAddress()
        saveBillingPostData.data.billingNewAddress = newAddress
        saveBillingPostData.formValues = formValues.formValues

        isLoadingLD.postValue(true)

        model.saveNewBilling(saveBillingPostData, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
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
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }

    fun saveNewShippingVM(
        newAddress: AddressModel,
        formValues: KeyValueFormData,
        model: CheckoutModel
    )
    {
        // remove custom attribute values from requestBody to avoid 500
        newAddress.customAddressAttributes = listOf()

        val saveShippingPostData = SaveShippingPostData()
        saveShippingPostData.data = ShippingAddress()
        saveShippingPostData.data.shippingNewAddress = newAddress
        saveShippingPostData.formValues = formValues.formValues

        isLoadingLD.postValue(true)

        model.saveNewShipping(saveShippingPostData, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }

    private fun getKeyValue(key: String, value: Long) =
        KeyValuePair(
            key = key,
            value = value.toString()
        )

    fun saveShippingFromExistingAddressVM(type: Int, addressId: Long, model: CheckoutModel)
    {
        var existingAddress = ExistingAddress()

        when(type)
        {
            CheckoutConstants.BillingAddress -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue(key = "billing_address_id", value = addressId)),
                    data = Data(shipToSameAddress = false))
            }

            CheckoutConstants.ShippingAddress -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue(key = "shipping_address_id",value =  addressId)),
                    data = Data(shipToSameAddress = false))
            }

            CheckoutConstants.StorePickUp -> {
                existingAddress = ExistingAddress(
                    formValues = listOf(getKeyValue(key = "pickup-points-id", value = addressId)),
                    data = Data(pickupInStore = true)
                )
            }

        }

        isLoadingLD.postValue(true)


        model.saveExistingShipping(existingAddress, object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String)  = hideLoader_ShowErrorMsg(errorMessage)
        })
    }



    fun saveShippingMethodVM(value: String, model: CheckoutModel)
    {
        val formValues = listOf(
            KeyValuePair(
                key = "shippingoption",
                value = value
            )
        )

        isLoadingLD.postValue(true)

        model.saveShippingMethod(KeyValueFormData(formValues), object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }

    fun savePaymentMethodVM(value: String, useRewardPoints: Boolean, model: CheckoutModel)
    {
        val formValues = mutableListOf(
            KeyValuePair(
                key = "paymentmethod",
                value = value
            )
        )

        if(useRewardPoints)
            formValues.add(
                KeyValuePair(
                    key = "UseRewardPoints",
                    value = useRewardPoints.toString()
                )
            )

        isLoadingLD.postValue(true)

        model.savePaymentMethod(KeyValueFormData(formValues), object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }


    fun getCheckoutConfirmInformationVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.getCheckoutConfirmInformation(object : RequestCompleteListener<ConfirmOrderResponse>
        {
            override fun onRequestSuccess(data: ConfirmOrderResponse)
            {
                isLoadingLD.postValue(false)
                getConfirmOrderLD.postValue(data)

            }

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }

    fun submitConfirmOrderVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.submitConfirmOrder(object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }
    fun submitCompleteOrderVM(model: CheckoutModel)
    {
        isLoadingLD.postValue(true)

        model.completeOrder(object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String) = hideLoader_ShowErrorMsg(errorMessage)
        })
    }

}