package com.bs.ecommerce.auth.login.data


import com.bs.ecommerce.product.model.data.CustomProperties
import com.google.gson.annotations.SerializedName

data class ChangePasswordData(
    @SerializedName("OldPassword")
    var oldPassword: String?,
    @SerializedName("NewPassword")
    var newPassword: String?,
    @SerializedName("ConfirmNewPassword")
    var confirmNewPassword: String?,
    @SerializedName("Result")
    val result: Any? = null,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = null
)