package com.bs.ecommerce.account.orders.model

import com.bs.ecommerce.account.orders.model.data.ReturnReqFormData
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import java.io.File

interface ReturnReqModel {

    fun getReturnReqFormData(
        orderId: Int,
        callback: RequestCompleteListener<ReturnReqFormData>
    )

    fun postReturnReqFormData(
        orderId: Int,
        reqBody: ReturnReqFormData,
        callback: RequestCompleteListener<ReturnReqFormData>
    )

    fun uploadFile(
        file: File,
        mimeType: String?,
        callback: RequestCompleteListener<UploadFileData>
    )
}