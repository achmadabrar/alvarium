package com.bs.ecommerce.main.model

import com.bs.ecommerce.auth.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.BaseResponse

interface AuthModel
{
    fun getRegisterInfo(callback: RequestCompleteListener<GetRegistrationResponse>)

    fun getAppLandingSettings(callback: RequestCompleteListener<AppLandingSettingResponse>)
}