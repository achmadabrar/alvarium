package com.bs.ecommerce.auth.register

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.CategoryTreeResponse


class RegistrationViewModel  : BaseViewModel()
{

    var getRegistrationResponseLD = MutableLiveData<GetRegistrationResponse>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()


    var appSettingsLD = MutableLiveData<AppLandingData>()


    fun getRegistrationData(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getRegisterInfo(object : RequestCompleteListener<GetRegistrationResponse>
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


