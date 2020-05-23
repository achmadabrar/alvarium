package com.bs.ecommerce.checkout

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModel
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.networking.common.Data
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData

open class CheckoutViewModel : BaseViewModel()
{

    var getBillingAddressLD = MutableLiveData<BillingAddressResponse>()

    var stateListLD = MutableLiveData<List<AvailableState>>()

    var saveResponseLD = MutableLiveData<CheckoutSaveResponse>()

    var shippingMethodModelLD = MutableLiveData<ShippingMethodModel>()

    var paymentMethodModelLD = MutableLiveData<PaymentMethodModel>()

    var getConfirmOrderLD = MutableLiveData<ConfirmOrderResponse>()


    private fun saveCheckoutData(data: CheckoutSaveResponse)
    {
        isLoadingLD.postValue(false)
        saveResponseLD.postValue(data)
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
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

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
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

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
            override fun onRequestSuccess(data: CheckoutSaveResponse) = saveCheckoutData(data)

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    private fun getKeyValue(key: String, value: Long) = KeyValuePair(key = key, value = value.toString())

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

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }
        })
    }



    fun saveShippingMethodVM(value: String, model: CheckoutModel)
    {
        val formValues = listOf(KeyValuePair(key = "shippingoption", value = value))

        isLoadingLD.postValue(true)

        model.saveShippingMethod(KeyValueFormData(formValues), object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse)
            {
                with(data.data.paymentMethodModel!!)
                {
                    if(displayRewardPoints)
                    {
                        this.paymentMethods.add(PaymentMethod(description = "", fee = rewardPointsBalance, logoUrl = "", name = "Reward Points", paymentMethodSystemName = "", selected = false ))
                    }
                    shippingMethodModelLD.postValue(data.data.shippingMethodModel)
                }
                saveCheckoutData(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun savePaymentMethodVM(value: String, model: CheckoutModel)
    {
        val formValues = listOf(KeyValuePair(key = "paymentmethod", value = value))

        isLoadingLD.postValue(true)

        model.savePaymentMethod(KeyValueFormData(formValues), object : RequestCompleteListener<CheckoutSaveResponse>
        {
            override fun onRequestSuccess(data: CheckoutSaveResponse){

                saveCheckoutData(data)
                paymentMethodModelLD.postValue(data.data.paymentMethodModel)
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

        model.getCheckoutConfirmInformation(object : RequestCompleteListener<ConfirmOrderResponse>
        {
            override fun onRequestSuccess(data: ConfirmOrderResponse)
            {
                isLoadingLD.postValue(false)
                getConfirmOrderLD.postValue(data)

            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

}