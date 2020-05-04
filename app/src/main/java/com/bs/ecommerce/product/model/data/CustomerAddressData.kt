package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class CustomerAddressData(
    @SerializedName("Addresses")
    val addresses: List<AddressModel>?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
)