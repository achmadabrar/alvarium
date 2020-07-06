package com.bs.ecommerce.more.contactus.data


import com.bs.ecommerce.more.contactus.data.ContactUsData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class ContactUsResponse(
    @SerializedName("Data")
    val contactUsData: ContactUsData?
): BaseResponse()