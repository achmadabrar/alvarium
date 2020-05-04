package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class EditCustomerAddressData(
    @SerializedName("Address")
    val address: AddressModel?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
)