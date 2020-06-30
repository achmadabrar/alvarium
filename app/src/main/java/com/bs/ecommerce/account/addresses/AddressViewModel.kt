package com.bs.ecommerce.account.addresses

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.checkout.CheckoutViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.addresses.model.CustomerAddressModel
import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.account.addresses.model.data.CustomerAddressData
import com.bs.ecommerce.account.addresses.model.data.EditCustomerAddressData

class AddressViewModel: CheckoutViewModel() {

    val addressListLD = MutableLiveData<CustomerAddressData?>()

    val addressLD = MutableLiveData<AddressModel?>()

    val resetFormLD = MutableLiveData(false)
    val addressDeleteLD = MutableLiveData(-1)

    var isEditMode: Boolean = false

    fun getCustomerAddresses(model: CustomerAddressModel) {

        isLoadingLD.value = true

        model.getCustomerAddresses(object :
            RequestCompleteListener<CustomerAddressData> {
            override fun onRequestSuccess(data: CustomerAddressData) {
                isLoadingLD.value = false

                addressListLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getNewAddressFormData(model: CustomerAddressModel) {

        isLoadingLD.value = true

        model.getFormDataForNewAddress(object :
            RequestCompleteListener<EditCustomerAddressData> {
            override fun onRequestSuccess(data: EditCustomerAddressData) {
                isLoadingLD.value = false

                addressLD.value = data.address
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getExistingAddressFormData(addressId: Int, model: CustomerAddressModel) {

        isLoadingLD.value = true

        model.getFormDataForEditAddress(addressId, object :
            RequestCompleteListener<EditCustomerAddressData> {
            override fun onRequestSuccess(data: EditCustomerAddressData) {
                isLoadingLD.value = false

                addressLD.value = data.address
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun saveCustomerAddress(address: AddressModel, customAttributes: List<KeyValuePair>, model: CustomerAddressModel) {

        if(isLoadingLD.value == true)
            return

        isLoadingLD.value = true

        model.saveAddress(address, customAttributes, object :
            RequestCompleteListener<Any?> {
            override fun onRequestSuccess(data: Any?) {
                isLoadingLD.value = false

                resetFormLD.value = true
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun updateAddress(address: AddressModel, customAttributes: List<KeyValuePair>, model: CustomerAddressModel) {

        if(isLoadingLD.value == true)
            return

        isLoadingLD.value = true

        model.updateAddress(address, customAttributes, object :
            RequestCompleteListener<Any?> {
            override fun onRequestSuccess(data: Any?) {
                isLoadingLD.value = false
                resetFormLD.value = true
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun deleteAddress(address: AddressModel, positionInList: Int, model: CustomerAddressModel) {
        if(isLoadingLD.value == true || address.id==null)
            return

         isLoadingLD.value = true

        model.deleteAddress(address.id!!, object :
            RequestCompleteListener<Any?> {
            override fun onRequestSuccess(data: Any?) {
                isLoadingLD.value = false
                addressDeleteLD.value = positionInList

                // to avoid remove item repetitively
                addressDeleteLD.value = -1
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                addressDeleteLD.value = -1

                toast(errorMessage)
            }

        })
    }
}