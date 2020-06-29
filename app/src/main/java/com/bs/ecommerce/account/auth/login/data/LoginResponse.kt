package com.bs.ecommerce.account.auth.login.data
import com.bs.ecommerce.account.auth.register.data.CustomerInfo
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class LoginResponse(@SerializedName("Data") var loginData: LoginResponseData = LoginResponseData()) : BaseResponse()


data class LoginResponseData(
    @SerializedName("CustomerInfo") var customerInfo: CustomerInfo = CustomerInfo(),
    @SerializedName("Token") var token: String? = null
)