package com.bs.ecommerce.auth.login.data
import com.google.gson.annotations.SerializedName


data class LoginPostData(
    @SerializedName("Data") var data: LoginData = LoginData(),
    @SerializedName("FormValues") var formValues: Any = Any(),
    @SerializedName("UploadPicture") var uploadPicture: Any = Any()
)

data class LoginData(
    @SerializedName("CheckoutAsGuest") var checkoutAsGuest: Boolean = false,
    @SerializedName("DisplayCaptcha") var displayCaptcha: Boolean = false,
    @SerializedName("Email") var email: String = "",
    @SerializedName("Password") var password: String = "",
    @SerializedName("RegistrationType") var registrationType: Int = 0,
    @SerializedName("RememberMe") var rememberMe: Boolean = false,
    @SerializedName("Username") var username: String = "",
    @SerializedName("UsernamesEnabled") var usernamesEnabled: Boolean = false
)