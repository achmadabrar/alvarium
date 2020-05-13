package com.bs.ecommerce.auth

import com.bs.ecommerce.auth.login.data.*
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.utils.TextUtils
import com.google.gson.GsonBuilder
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

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), GetRegistrationResponse::class.java)

                    callback.onRequestSuccess(errorBody as GetRegistrationResponse)
                }
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

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), LoginResponse::class.java)

                    callback.onRequestSuccess(errorBody as LoginResponse)
                }
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }
        })
    }


    override fun getCustomerInfoModel(callback: RequestCompleteListener<GetRegistrationResponse>)
    {

        RetroClient.api.getCustomerInfoAPI().enqueue(object : Callback<GetRegistrationResponse>
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

    override fun forgotPassword(
        userData: ForgotPasswordData,
        callback: RequestCompleteListener<ForgotPasswordResponse>
    ) {
        val reqBody = ForgotPasswordResponse(userData)

        RetroClient.api.forgetPassword(reqBody).enqueue(object : Callback<ForgotPasswordResponse> {

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ForgotPasswordResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }

    override fun changePassword(
        userData: ChangePasswordData,
        callback: RequestCompleteListener<ChangePasswordResponse>
    ) {
        val reqBody = ChangePasswordResponse(userData)

        RetroClient.api.changePassword(reqBody).enqueue(object : Callback<ChangePasswordResponse> {

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ChangePasswordResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }


}