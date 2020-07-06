package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @SerializedName("Data")
    val data: UploadFileData?
): BaseResponse()

data class UploadFileData(
    @SerializedName("DownloadGuid")
    val downloadGuid: String?,
    @SerializedName("DownloadUrl")
    val downloadUrl: String?
)