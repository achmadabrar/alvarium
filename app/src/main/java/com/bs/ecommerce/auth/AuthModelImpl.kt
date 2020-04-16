package com.bs.ecommerce.auth

import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.main.model.AuthModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthModelImpl: AuthModel
{

    override fun getRegisterModel(callback: RequestCompleteListener<GetRegistrationResponse>)
    {

        RetroClient.api.getRegisterAPI().enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }

    override fun postRegisterModel(registerPostData: GetRegistrationResponse, callback: RequestCompleteListener<GetRegistrationResponse>)
    {

        RetroClient.api.postRegisterAPI(registerPostData).enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }


    override fun postLoginModel(loginPostData : LoginPostData, callback: RequestCompleteListener<LoginResponse>)
    {

        RetroClient.api.postLoginAPI(loginPostData).enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as LoginResponse)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }


}