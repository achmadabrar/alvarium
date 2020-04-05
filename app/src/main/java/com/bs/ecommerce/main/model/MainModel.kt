package com.bs.ecommerce.main.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.BaseResponse

interface MainModel
{
    fun getLeftCategories(callback: RequestCompleteListener<CategoryTreeResponse>)

    fun getAppLandingSettings(callback: RequestCompleteListener<AppLandingSettingResponse>)
}