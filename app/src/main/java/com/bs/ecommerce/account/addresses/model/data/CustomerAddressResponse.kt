package com.bs.ecommerce.account.addresses.model.data


import com.bs.ecommerce.account.addresses.model.data.CustomerAddressData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class CustomerAddressResponse(
    @SerializedName("Data")
    val `data`: CustomerAddressData?
): BaseResponse()