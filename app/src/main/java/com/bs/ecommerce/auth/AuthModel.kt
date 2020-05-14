package com.bs.ecommerce.auth

import com.bs.ecommerce.auth.login.data.*
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener

interface AuthModel
{
    fun getRegisterModel(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postRegisterModel(registerPostData: GetRegistrationResponse, callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postLoginModel(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)

    fun getCustomerInfoModel(callback: RequestCompleteListener<GetRegistrationResponse>)

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