package com.bs.ecommerce.auth.forgotpass

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.login.data.ForgotPasswordData
import com.bs.ecommerce.auth.login.data.ForgotPasswordResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener

class PasswordViewModel: BaseViewModel() {

    val actionSuccessLD = MutableLiveData<Boolean>()

    fun requestForgetPassword(email: String, model: AuthModel) {

        isLoadingLD.value = true

        val data = ForgotPasswordData(email, null, false, null)

        model.forgotPassword(data, object : RequestCompleteListener<ForgotPasswordResponse> {

            override fun onRequestSuccess(data: ForgotPasswordResponse) {
                isLoadingLD.value = false
                actionSuccessLD.value = true
                toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                actionSuccessLD.value = false
                toast(errorMessage)
            }

        })
    }
}