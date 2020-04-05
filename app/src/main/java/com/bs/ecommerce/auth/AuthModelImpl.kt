package com.bs.ecommerce.main.model

import android.content.Context
import com.bs.ecommerce.auth.data.GetRegistrationResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthModelImpl(private val context: Context): AuthModel
{

    override fun getRegisterInfo(callback: RequestCompleteListener<GetRegistrationResponse>)
    {

        RetroClient.api.getRegisterData().enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
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