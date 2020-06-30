package com.bs.ecommerce.account.addresses.model.data


import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class EditCustomerAddressData(
    @SerializedName("Address")
    val address: AddressModel?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
)