package com.bs.ecommerce.account.orders.model

import com.bs.ecommerce.account.orders.model.data.ReturnReqFormData
import com.bs.ecommerce.account.orders.model.data.ReturnReqFormResponse
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.account.orders.model.data.UploadFileResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.utils.ProgressRequestBody
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ReturnReqModelImpl: ReturnReqModel {

    override fun getReturnReqFormData(
        orderId: Int,
        callback: RequestCompleteListener<ReturnReqFormData>
    ) {
        RetroClient.api.getReturnReqFormData(orderId).enqueue(object :
            Callback<ReturnReqFormResponse> {

            override fun onFailure(call: Call<ReturnReqFormResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ReturnReqFormResponse>,
                response: Response<ReturnReqFormResponse>
            ) {
                if (response.body()?.data != null && response.code() == 200)
                    callback.onRequestSuccess(response.body()?.data as ReturnReqFormData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }


    override fun uploadFile(file: File, callback: RequestCompleteListener<UploadFileData>) {

        val requestBody = ProgressRequestBody(
            file, null, object: ProgressRequestBody.ProgressCallback {
                override fun onProgress(progress: Long, total: Long) {
                    "upload_percent".showLog("$progress - $total")
                }
            })

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestBody)


        RetroClient.api.uploadFile(body).enqueue(object :
            Callback<UploadFileResponse> {

            override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<UploadFileResponse>,
                response: Response<UploadFileResponse>
            ) {
                if (response.body()?.data != null && response.code() == 200)
                    callback.onRequestSuccess(response.body()?.data as UploadFileData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }
}