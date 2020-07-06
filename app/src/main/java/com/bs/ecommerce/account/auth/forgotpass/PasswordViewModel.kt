package com.bs.ecommerce.account.auth.forgotpass

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.AuthModel
import com.bs.ecommerce.account.auth.login.data.ChangePasswordModel
import com.bs.ecommerce.account.auth.login.data.ForgotPasswordData
import com.bs.ecommerce.account.auth.login.data.ForgotPasswordResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener

class PasswordViewModel: BaseViewModel() {

    val actionSuccessLD = MutableLiveData<Boolean>()

    val changePasswordLD = MutableLiveData<ChangePasswordModel>()

    fun requestForgetPassword(email: String, model: AuthModel) {

        isLoadingLD.value = true

        val data = ForgotPasswordData(
            email,
            null,
            false,
            null
        )

        model.forgotPassword(data, object :
            RequestCompleteListener<ForgotPasswordResponse> {

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

    fun getChangePasswordModel(model: AuthModel) {
        isLoadingLD.value = true

        model.getChangePassword(object :
            RequestCompleteListener<ChangePasswordModel> {

            override fun onRequestSuccess(data: ChangePasswordModel) {

                isLoadingLD.value = false

                changePasswordLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false

                toast(errorMessage)
            }

        })
    }

    fun postChangePasswordModel(userData: ChangePasswordModel, model: AuthModel) {
        isLoadingLD.value = true

        model.postChangePassword(userData, object :
            RequestCompleteListener<ChangePasswordModel> {

            override fun onRequestSuccess(data: ChangePasswordModel) {

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