package com.bs.ecommerce.main.model

import android.content.Context
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModelImpl(private val context: Context): MainModel
{

    override fun getLeftCategories(callback: RequestCompleteListener<CategoryTreeResponse>)
    {

        RetroClient.api.getHomeCategoryTree().enqueue(object : Callback<CategoryTreeResponse>
        {
            override fun onResponse(call: Call<CategoryTreeResponse>, response: Response<CategoryTreeResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CategoryTreeResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
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
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<AppLandingSettingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

}