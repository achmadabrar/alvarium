package com.bs.ecommerce.more.vendor

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.VendorModel
import com.bs.ecommerce.product.model.data.ContactVendorModel
import com.bs.ecommerce.product.model.data.GetAllVendorsResponse
import com.bs.ecommerce.product.model.data.GetContactVendorResponse
import com.bs.ecommerce.product.model.data.ProductByVendorData
import com.bs.ecommerce.utils.OneTimeEvent

class VendorViewModel : BaseViewModel() {

    var vendorLD = MutableLiveData<List<ProductByVendorData>>()
    var contactVendorModelLD = MutableLiveData<ContactVendorModel?>()
    var enquirySendSuccessLD = MutableLiveData<OneTimeEvent<Boolean>>()

    fun getAllVendors(model: VendorModel) {
        isLoadingLD.value = true

        model.getAllVendors(object : RequestCompleteListener<GetAllVendorsResponse> {
            override fun onRequestSuccess(data: GetAllVendorsResponse) {
                isLoadingLD.value = false

                vendorLD.value  = data.data ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

    fun getContactVendorModel(vendorId: Int, model: VendorModel) {
        isLoadingLD.value = true

        model.getContactVendorModel(vendorId, object : RequestCompleteListener<GetContactVendorResponse> {

            override fun onRequestSuccess(data: GetContactVendorResponse) {
                isLoadingLD.value = false

                contactVendorModelLD.value  = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

    fun postVendorEnquiry(data: ContactVendorModel, model: VendorModel) {
        isLoadingLD.value = true

        model.postContactVendorModel(
            GetContactVendorResponse(data),
            object : RequestCompleteListener<GetContactVendorResponse> {

                override fun onRequestSuccess(data: GetContactVendorResponse) {
                    isLoadingLD.value = false
                    toast(data.message)

                    enquirySendSuccessLD.value = OneTimeEvent(true)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.value = false
                    toast(errorMessage)
                }
            })
    }
}