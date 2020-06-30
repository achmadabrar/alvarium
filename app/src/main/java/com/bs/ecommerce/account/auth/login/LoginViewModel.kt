package com.bs.ecommerce.account.auth.login

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.AuthModel
import com.bs.ecommerce.account.auth.login.data.LoginPostData
import com.bs.ecommerce.account.auth.login.data.LoginResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener


class LoginViewModel  : BaseViewModel()
{

    var loginResponseLD = MutableLiveData<LoginResponse>()
    var logoutResponseLD = MutableLiveData<Boolean>()

    fun postLoginVM(loginPostData : LoginPostData, model: AuthModel)
    {

        isLoadingLD.value = true

        model.postLoginModel(loginPostData, object :
            RequestCompleteListener<LoginResponse>
        {
            override fun onRequestSuccess(data: LoginResponse)
            {
                isLoadingLD.value = false
                loginResponseLD.value = data
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }


    fun logout(model: AuthModel) {

        isLoadingLD.value = true

        model.logout(object :
            RequestCompleteListener<Boolean> {
            override fun onRequestSuccess(data: Boolean) {
                isLoadingLD.value = false
                logoutResponseLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

}


