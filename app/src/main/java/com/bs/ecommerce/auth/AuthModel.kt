package com.bs.ecommerce.auth

import com.bs.ecommerce.auth.login.data.*
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.common.RequestCompleteListener

interface AuthModel
{
    fun getRegisterModel(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postRegisterModel(registerPostData: GetRegistrationResponse, callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postLoginModel(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)

    fun getCustomerInfoModel(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun forgotPassword(
        userData: ForgotPasswordData,
        callback: RequestCompleteListener<ForgotPasswordResponse>
    )

    fun changePassword(
        userData: ChangePasswordData,
        callback: RequestCompleteListener<ChangePasswordResponse>
    )
}