package com.bs.ecommerce.main.model

import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.common.RequestCompleteListener

interface AuthModel
{
    fun getRegisterModel(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postRegisterModel(registerPostData: GetRegistrationResponse, callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postLoginModel(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)

}