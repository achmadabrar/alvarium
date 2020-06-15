package com.bs.ecommerce.auth.login

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener


class LoginViewModel  : BaseViewModel()
{

    var loginResponseLD = MutableLiveData<LoginResponse>()


    fun postLoginVM(loginPostData : LoginPostData, model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.postLoginModel(loginPostData, object : RequestCompleteListener<LoginResponse>
        {
            override fun onRequestSuccess(data: LoginResponse)
            {
                isLoadingLD.postValue(false)

                loginResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                toast(errorMessage)
            }
        })
    }


}


