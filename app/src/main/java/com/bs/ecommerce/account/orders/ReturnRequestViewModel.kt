package com.bs.ecommerce.account.orders

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.orders.model.ReturnReqModel
import com.bs.ecommerce.account.orders.model.data.ReturnReqFormData
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import java.io.File

class ReturnRequestViewModel: BaseViewModel() {

    val returnReqLD = MutableLiveData<ReturnReqFormData>()
    val uploadFileLD = MutableLiveData<UploadFileData>()

    fun getFormData(orderId: Int, model: ReturnReqModel) {

        isLoadingLD.value = true

        model.getReturnReqFormData(orderId, object :
            RequestCompleteListener<ReturnReqFormData> {

            override fun onRequestSuccess(data: ReturnReqFormData) {
                isLoadingLD.value = false
                returnReqLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }

        })
    }

    fun uploadFile(file: File, model: ReturnReqModel) {
        isLoadingLD.value = true

        model.uploadFile(file, object :
            RequestCompleteListener<UploadFileData> {

            override fun onRequestSuccess(data: UploadFileData) {
                isLoadingLD.value = false
                uploadFileLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }

        })
    }
}