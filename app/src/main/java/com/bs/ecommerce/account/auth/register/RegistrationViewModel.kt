package com.bs.ecommerce.account.auth.register

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.AuthModel
import com.bs.ecommerce.account.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.checkout.CheckoutViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener


class RegistrationViewModel  : CheckoutViewModel()
{

    var getRegistrationResponseLD = MutableLiveData<GetRegistrationResponse>()
    val registrationSuccessLD = MutableLiveData<Boolean>()

    var customerInfoUpdate = false

    fun getRegistrationVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getRegisterModel(object :
            RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                toast(errorMessage)
            }
        })
    }



    fun getCustomerInfoVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getCustomerInfoModel(object :
            RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)

            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                toast(errorMessage)
            }
        })
    }

    fun postRegisterVM(registerPostData : GetRegistrationResponse, model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.postRegisterModel(registerPostData, object :
            RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)
                toast(data.message)

                registrationSuccessLD.value = true
                //getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                registrationSuccessLD.value = false

                toast(errorMessage)
            }
        })
    }

    fun postCustomerInfo(registerPostData: GetRegistrationResponse, model: AuthModel) {

        isLoadingLD.postValue(true)

        model.postCustomerInfoModel(
            registerPostData,

            object :
                RequestCompleteListener<GetRegistrationResponse> {
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


