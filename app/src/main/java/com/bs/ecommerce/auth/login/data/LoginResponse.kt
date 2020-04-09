package com.bs.ecommerce.auth.login.data
import com.bs.ecommerce.auth.register.data.CustomerInfo
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("Data") var loginData: LoginResponseData = LoginResponseData(),
    @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
    @SerializedName("Message") var message: Any = Any()
)

data class LoginResponseData(
    @SerializedName("CustomerInfo") var customerInfo: CustomerInfo = CustomerInfo(),
    @SerializedName("Token") var token: String = ""
)

