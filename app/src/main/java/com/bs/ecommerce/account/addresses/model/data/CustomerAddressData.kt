package com.bs.ecommerce.account.addresses.model.data


import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class CustomerAddressData(
    @SerializedName("Addresses")
    val addresses: List<AddressModel>?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
)