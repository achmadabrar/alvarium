package com.bs.ecommerce.auth.register

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener


class RegistrationViewModel  : BaseViewModel()
{

    var getRegistrationResponseLD = MutableLiveData<GetRegistrationResponse>()
    val actionSuccessLD = MutableLiveData<Boolean>()

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



    fun getCustomerInfoVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getCustomerInfoModel(object : RequestCompleteListener<GetRegistrationResponse>
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
                toast(data.message)

                actionSuccessLD.value = true
                //getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                actionSuccessLD.value = false
            }
        })
    }

    fun postCustomerInfo(registerPostData: GetRegistrationResponse, model: AuthModel) {

        isLoadingLD.postValue(true)

        model.postCustomerInfoModel(
            registerPostData,

            object : RequestCompleteListener<GetRegistrationResponse> {
                override fun onRequestSuccess(data: GetRegistrationResponse) {
                    isLoadingLD.postValue(false)

                    getRegistrationResponseLD.postValue(data)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.postValue(false)
                    toast(errorMessage)
                }
            })
    }

}


