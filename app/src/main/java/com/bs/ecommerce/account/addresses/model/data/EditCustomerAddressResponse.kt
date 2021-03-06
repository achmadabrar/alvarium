package com.bs.ecommerce.account.addresses.model.data

import com.bs.ecommerce.account.addresses.model.data.EditCustomerAddressData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class EditCustomerAddressResponse (
    @SerializedName("Data")
    var `data`: EditCustomerAddressData?
): BaseResponse()