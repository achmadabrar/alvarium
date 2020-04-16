package com.bs.ecommerce.auth.register

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.GetRegisterData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel


class RegistrationViewModel  : BaseViewModel()
{

    var getRegistrationResponseLD = MutableLiveData<GetRegistrationResponse>()


    fun getRegistrationVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getRegisterModel(object : RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun postRegisterVM(registerPostData : GetRegistrationResponse, model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.postRegisterModel(registerPostData, object : RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

}


