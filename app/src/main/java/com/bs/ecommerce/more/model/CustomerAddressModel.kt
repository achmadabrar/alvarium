package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.AddressModel
import com.bs.ecommerce.product.model.data.CustomerAddressData
import com.bs.ecommerce.product.model.data.EditCustomerAddressData

interface CustomerAddressModel {

    fun getCustomerAddresses(
        callback: RequestCompleteListener<CustomerAddressData>
    )

    fun getFormDataForNewAddress(
        callback: RequestCompleteListener<EditCustomerAddressData>
    )

    fun getFormDataForEditAddress(
        addressId: Int,
        callback: RequestCompleteListener<EditCustomerAddressData>
    )

    fun saveAddress(
        address: AddressModel,
        callback: RequestCompleteListener<Any?>
    )

    fun deleteAddress(
        addressId: Int,
        callback: RequestCompleteListener<Any?>
    )
}