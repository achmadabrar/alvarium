package com.bs.ecommerce.auth.login.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("Data")
    var `data`: ForgotPasswordData?
): BaseResponse()