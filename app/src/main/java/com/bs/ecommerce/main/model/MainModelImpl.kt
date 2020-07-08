package com.bs.ecommerce.main.model

import android.content.Context
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.account.orders.model.data.UploadFileResponse
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.AppStartRequest
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.utils.ProgressRequestBody
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainModelImpl: MainModel
{

    override fun getLeftCategories(callback: RequestCompleteListener<CategoryTreeResponse>)
    {

        RetroClient.api.getHomeCategoryTree().enqueue(object : Callback<CategoryTreeResponse>
        {
            override fun onResponse(call: Call<CategoryTreeResponse>, response: Response<CategoryTreeResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CategoryTreeResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CategoryTreeResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }


    override fun getAppLandingSettings(callback: RequestCompleteListener<AppLandingSettingResponse>)
    {

        RetroClient.api.getAppLandingSettings().enqueue(object : Callback<AppLandingSettingResponse>
        {
            override fun onResponse(call: Call<AppLandingSettingResponse>, response: Response<AppLandingSettingResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as AppLandingSettingResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<AppLandingSettingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }

    override fun changeLanguage(languageId : Long, callback: RequestCompleteListener<BaseResponse>)
    {
        RetroClient.api.setLanguage(languageId).enqueue(object : Callback<BaseResponse>
        {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as BaseResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }

    override fun changeCurrency(currencyId : Long, callback: RequestCompleteListener<BaseResponse>)
    {

        RetroClient.api.setCurrency(currencyId).enqueue(object : Callback<BaseResponse>
        {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as BaseResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }

    override fun submitAppStart(appStartRequest: AppStartRequest, callback: RequestCompleteListener<Any?>)
    {

        RetroClient.api.appStartApi(appStartRequest).enqueue(object : Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                if (response.code() == 200)
                    callback.onRequestSuccess(null)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }

    override fun uploadFile(file: File, mimeType: String?, callback: RequestCompleteListener<UploadFileData>) {

        val requestBody = ProgressRequestBody(
            file, mimeType, object: ProgressRequestBody.ProgressCallback {
                override fun onProgress(progress: Long, total: Long) {
                    "upload_percent".showLog("$progress - $total")
                }
            })

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestBody)


        RetroClient.api.uploadFile(body).enqueue(object :
            Callback<UploadFileResponse> {

            override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<UploadFileResponse>,
                response: Response<UploadFileResponse>
            ) {
                if (response.body()?.data != null && response.code() == 200)
                    callback.onRequestSuccess(response.body()?.data as UploadFileData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

}