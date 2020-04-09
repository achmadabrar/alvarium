package com.bs.ecommerce.main.model

import android.content.Context
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
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

    override fun postLoginInfo(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)
    {

        RetroClient.api.performLogin(loginPostData).enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }


}