package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class ReturnReqFormData(
    @SerializedName("AllowFiles")
    val allowFiles: Boolean?,
    @SerializedName("AvailableReturnActions")
    val availableReturnActions: List<AvailableReturnAction>?,
    @SerializedName("AvailableReturnReasons")
    val availableReturnReasons: List<AvailableReturnReason>?,
    @SerializedName("Comments")
    val comments: String?,
    @SerializedName("CustomOrderNumber")
    val customOrderNumber: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Items")
    val items: List<ReturnReqProductItem>?,
    @SerializedName("OrderId")
    val orderId: Int?,
    @SerializedName("Result")
    val result: String?,
    @SerializedName("ReturnRequestActionId")
    val returnRequestActionId: Int?,
    @SerializedName("ReturnRequestReasonId")
    val returnRequestReasonId: Int?,
    @SerializedName("UploadedFileGuid")
    val uploadedFileGuid: String?
)