package com.bs.ecommerce.main.model

import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.AppStartRequest
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.common.BaseResponse
import java.io.File

interface MainModel
{
    fun getLeftCategories(callback: RequestCompleteListener<CategoryTreeResponse>)

    fun getAppLandingSettings(callback: RequestCompleteListener<AppLandingSettingResponse>)

    fun changeLanguage(languageId : Long, callback: RequestCompleteListener<BaseResponse>)

    fun changeCurrency(currencyId : Long, callback: RequestCompleteListener<BaseResponse>)

    fun submitAppStart(appStartRequest: AppStartRequest, callback: RequestCompleteListener<Any?>)

    fun uploadFile(
        file: File,
        mimeType: String?,
        callback: RequestCompleteListener<UploadFileData>
    )
}