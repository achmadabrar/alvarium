package com.bs.ecommerce.auth

import com.bs.ecommerce.auth.login.data.*
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.TextUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthModelImpl: AuthModel
{

    override fun getRegisterModel(callback: RequestCompleteListener<GetRegistrationResponse>) {

        RetroClient.api.getRegisterAPI().enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun postRegisterModel(
        registerPostData: GetRegistrationResponse,
        callback: RequestCompleteListener<GetRegistrationResponse>
    ) {

        RetroClient.api.postRegisterAPI(registerPostData).enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }


    override fun postLoginModel(
        loginPostData: LoginPostData,
        callback: RequestCompleteListener<LoginResponse>
    ) {

        RetroClient.api.postLoginAPI(loginPostData).enqueue(object : Callback<LoginResponse>
        {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>)
            {

                if (response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as LoginResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun logout(callback: RequestCompleteListener<Boolean>) {

        RetroClient.api.logout().enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 200)
                    callback.onRequestSuccess(data = true)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(
                    t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG)
                )
            }
        })
    }


    override fun getCustomerInfoModel(callback: RequestCompleteListener<GetRegistrationResponse>) {

        RetroClient.api.getCustomerInfoAPI().enqueue(object : Callback<GetRegistrationResponse>
        {
            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun postCustomerInfoModel(
        postData: GetRegistrationResponse,
        callback: RequestCompleteListener<GetRegistrationResponse>
    ) {
        RetroClient.api.saveCustomerInfo(postData).enqueue(object : Callback<GetRegistrationResponse> {

            override fun onFailure(call: Call<GetRegistrationResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }

            override fun onResponse(call: Call<GetRegistrationResponse>, response: Response<GetRegistrationResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as GetRegistrationResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
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
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
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

    override fun getChangePassword(callback: RequestCompleteListener<ChangePasswordModel>) {

        RetroClient.api.getChangePasswordModel().enqueue(object : Callback<ChangePasswordModel> {

            override fun onFailure(call: Call<ChangePasswordModel>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }

            override fun onResponse(call: Call<ChangePasswordModel>, response: Response<ChangePasswordModel>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ChangePasswordModel)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }

    override fun postChangePassword(
        userData: ChangePasswordModel,
        callback: RequestCompleteListener<ChangePasswordModel>
    ) {

        RetroClient.api.changePassword(userData).enqueue(object : Callback<ChangePasswordModel> {

            override fun onFailure(call: Call<ChangePasswordModel>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }

            override fun onResponse(call: Call<ChangePasswordModel>, response: Response<ChangePasswordModel>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ChangePasswordModel)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }


}