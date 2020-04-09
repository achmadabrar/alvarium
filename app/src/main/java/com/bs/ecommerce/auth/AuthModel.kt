package com.bs.ecommerce.main.model

import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.common.RequestCompleteListener

interface AuthModel
{
    fun getRegisterInfo(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun postLoginInfo(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)

}