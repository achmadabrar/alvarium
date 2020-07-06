package com.bs.ecommerce.more.vendor.model.data


import com.bs.ecommerce.more.vendor.model.data.ContactVendorModel
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetContactVendorResponse(
    @SerializedName("Data")
    val `data`: ContactVendorModel?
): BaseResponse()