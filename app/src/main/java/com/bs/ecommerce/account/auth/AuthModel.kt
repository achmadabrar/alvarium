package com.bs.ecommerce.account.auth

import com.bs.ecommerce.account.auth.login.data.*
import com.bs.ecommerce.account.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.networking.common.RequestCompleteListener

interface AuthModel
{
    fun getRegisterModel(
        callback: RequestCompleteListener<GetRegistrationResponse>
    )

    fun postRegisterModel(
        registerPostData: GetRegistrationResponse,
        callback: RequestCompleteListener<GetRegistrationResponse>
    )

    fun postLoginModel(
        loginPostData: LoginPostData,
        callback: RequestCompleteListener<LoginResponse>
    )

    fun getLoginModel(
        callback: RequestCompleteListener<LoginPostData>
    )

    fun logout(
        callback: RequestCompleteListener<Boolean>
    )

    fun getCustomerInfoModel(
        callback: RequestCompleteListener<GetRegistrationResponse>
    )

    fun postCustomerInfoModel(
        postData: GetRegistrationResponse,
        callback: RequestCompleteListener<GetRegistrationResponse>
    )

    fun forgotPassword(
        userData: ForgotPasswordData,
        callback: RequestCompleteListener<ForgotPasswordResponse>
    )

    fun getChangePassword(
        callback: RequestCompleteListener<ChangePasswordModel>
    )

    fun postChangePassword(
        userData: ChangePasswordModel,
        callback: RequestCompleteListener<ChangePasswordModel>
    )
}