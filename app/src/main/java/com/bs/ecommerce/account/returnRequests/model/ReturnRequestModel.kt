package com.bs.ecommerce.account.returnRequests.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryData
import okhttp3.ResponseBody
import retrofit2.Response

interface ReturnRequestModel {

    fun getReturnRequestHistory(
        callback: RequestCompleteListener<ReturnRequestHistoryData>
    )


    fun downloadFile(
        guid: String,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )
}