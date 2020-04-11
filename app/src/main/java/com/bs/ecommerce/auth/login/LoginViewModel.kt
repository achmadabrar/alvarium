package com.bs.ecommerce.auth.login

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel


class LoginViewModel  : BaseViewModel()
{

    var loginResponseLD = MutableLiveData<LoginResponse>()


    fun postLoginInfo(loginPostData : LoginPostData, model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.postLoginInfo(loginPostData, object : RequestCompleteListener<LoginResponse>
        {
            override fun onRequestSuccess(data: LoginResponse)
            {
                isLoadingLD.postValue(false)

                loginResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }


}


