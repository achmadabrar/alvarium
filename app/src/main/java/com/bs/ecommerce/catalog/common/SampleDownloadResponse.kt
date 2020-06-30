package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class SampleDownloadResponse(
    @SerializedName("Data")
    val `data`: SampleDownloadData?
): BaseResponse()


data class SampleDownloadData(
    @SerializedName("DownloadUrl")
    val downloadUrl: String?,
    @SerializedName("HasUserAgreement")
    val hasUserAgreement: Boolean?,
    @SerializedName("OrderItemId")
    val orderItemId: String?,
    @SerializedName("Redirect")
    val redirect: Boolean?
)