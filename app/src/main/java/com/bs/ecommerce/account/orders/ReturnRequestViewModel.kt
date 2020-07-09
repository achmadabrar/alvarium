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
    //val uploadFileLD = MutableLiveData<UploadFileData?>()

    //var uploadedFileGuid: String = ""

    fun getFormData(orderId: Int, model: ReturnReqModel) {

        isLoadingLD.value = true

        model.getReturnReqFormData(orderId, object :
            RequestCompleteListener<ReturnReqFormData> {

            override fun onRequestSuccess(data: ReturnReqFormData) {
                isLoadingLD.value = false
                returnReqLD.value = data

                uploadedFileGuid = data.uploadedFileGuid ?: "00000000-0000-0000-0000-000000000000"
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }

        })
    }

    fun postFormData(orderId: Int, req: ReturnReqFormData, model: ReturnReqModel) {

        isLoadingLD.value = true

        model.postReturnReqFormData(orderId, req, object :
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

    fun uploadFile(
        file: File,
        mimeType: String?,
        model: ReturnReqModel
    ) {

        model.uploadFile(file, mimeType, object :
            RequestCompleteListener<UploadFileData> {

            override fun onRequestSuccess(data: UploadFileData) {
                uploadFileLD.value = data
                uploadedFileGuid = data.downloadGuid ?: ""

                try { file.delete() } catch (e: Exception) { e.printStackTrace() }
            }

            override fun onRequestFailed(errorMessage: String) {
                uploadFileLD.value = null
                uploadedFileGuid = ""

                try { file.delete() } catch (e: Exception) { e.printStackTrace() }
            }

        })
    }
}