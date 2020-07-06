package com.bs.ecommerce.more.contactus

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.more.options.model.CommonModel
import com.bs.ecommerce.more.contactus.data.ContactUsData
import com.bs.ecommerce.more.contactus.data.ContactUsResponse

class ContactUsViewModel: BaseViewModel() {

    val contactUsLD = MutableLiveData<ContactUsData?>()

    fun postCustomersEnquiry(userData: ContactUsData, model: CommonModel) {

        if(isLoadingLD.value == true) return

        isLoadingLD.value = true

        model.postCustomerEnquiry(userData, object:
            RequestCompleteListener<ContactUsResponse> {

            override fun onRequestSuccess(data: ContactUsResponse) {
                isLoadingLD.value = false

                if(data.contactUsData != null)
                    contactUsLD.value = data.contactUsData

                toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }


    fun getContactUsModel(model: CommonModel) {

        if(isLoadingLD.value == true) return

        isLoadingLD.value = true

        model.getContactUsModel(object:
            RequestCompleteListener<ContactUsResponse> {

            override fun onRequestSuccess(data: ContactUsResponse) {
                isLoadingLD.value = false

                contactUsLD.value = data.contactUsData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }
}