package com.bs.ecommerce.account.auth.login.data


import com.bs.ecommerce.product.model.data.CustomProperties
import com.google.gson.annotations.SerializedName

data class ForgotPasswordData(
    @SerializedName("Email")
    val email: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DisplayCaptcha")
    val displayCaptcha: Boolean?,
    @SerializedName("Result")
    val result: Any?
)