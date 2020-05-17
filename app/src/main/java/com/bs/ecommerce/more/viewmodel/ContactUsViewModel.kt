package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.model.CommonModel
import com.bs.ecommerce.product.model.data.ContactUsData
import com.bs.ecommerce.product.model.data.ContactUsResponse

class ContactUsViewModel: BaseViewModel() {

    val contactUsLD = MutableLiveData<ContactUsData>()

    fun postCustomersEnquiry(userData: ContactUsData, model: CommonModel) {

        if(isLoadingLD.value == true) return

        isLoadingLD.value = true

        model.postCustomerEnquiry(userData, object: RequestCompleteListener<ContactUsResponse> {

            override fun onRequestSuccess(data: ContactUsResponse) {
                isLoadingLD.value = false

                if(data.contactUsData != null)
                    contactUsLD.value = data.contactUsData

                toast(data.message ?: "")
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }
}