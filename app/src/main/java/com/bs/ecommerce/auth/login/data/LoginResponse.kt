package com.bs.ecommerce.auth.login.data
import com.bs.ecommerce.auth.register.data.CustomerInfo
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.utils.showLog
import com.google.gson.annotations.SerializedName


data class LoginResponse(@SerializedName("Data") var loginData: LoginResponseData = LoginResponseData()) : BaseResponse()


data class LoginResponseData(
    @SerializedName("CustomerInfo") var customerInfo: CustomerInfo = CustomerInfo(),
    @SerializedName("Token") var token: String? = null
)