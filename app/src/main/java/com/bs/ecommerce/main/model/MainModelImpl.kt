package com.bs.ecommerce.main.model

import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.AppStartRequest
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.utils.TextUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

}