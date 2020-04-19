package com.bs.ecommerce.auth.login.data
import com.bs.ecommerce.auth.register.data.CustomerInfo
import com.bs.ecommerce.utils.showLog
import com.google.gson.annotations.SerializedName


data class LoginResponse(@SerializedName("Data") var loginData: LoginResponseData = LoginResponseData()) : BaseResponse()


/*data class LoginResponse(@SerializedName("Data") var loginData: LoginResponseData = LoginResponseData(),
                         @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
                         @SerializedName("Message") var message: Any = Any())*/



data class LoginResponseData(
    @SerializedName("CustomerInfo") var customerInfo: CustomerInfo = CustomerInfo(),
    @SerializedName("Token") var token: String? = null
)

open class BaseResponse
{
    @SerializedName("Message") var message: Any? = Any()

    @SerializedName("ErrorList")  var errorList: Array<String> = arrayOf()

    val errorsAsFormattedString: String
        get()
        {
            var errors = ""

            if (errorList.isNotEmpty())
            {
                for (i in errorList.indices)
                {
                    errors += "  " + (i + 1) + ": " + errorList[i] + " \n"

                    "wewtet".showLog(errorList[i])
                }
            }
            return errors
        }
}