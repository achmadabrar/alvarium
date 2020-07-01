package com.bs.ecommerce.account.returnRequests.model.data
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class ReturnRequestHistoryResponse(
    @SerializedName("Data") var ReturnRequestHistoryData: ReturnRequestHistoryData? = null
)
    : BaseResponse()

data class ReturnRequestHistoryData(@SerializedName("Items") var items: List<ReturnRequestHistoryItem> = listOf())


data class ReturnRequestHistoryItem(
    @SerializedName("Comments") var comments: Any? = Any(),
    @SerializedName("CreatedOn") var createdOn: String? = "",
    @SerializedName("CustomNumber") var customNumber: String? = "",
    @SerializedName("Id") var id: Int? = 0,
    @SerializedName("ProductId") var productId: Int = 0,
    @SerializedName("ProductName") var productName: String? = "",
    @SerializedName("ProductSeName") var productSeName: String? = "",
    @SerializedName("Quantity") var quantity: Int? = 0,
    @SerializedName("ReturnAction") var returnAction: String? = "",
    @SerializedName("ReturnReason") var returnReason: String? = "",
    @SerializedName("ReturnRequestStatus") var returnRequestStatus: String? = "",
    @SerializedName("UploadedFileGuid") var uploadedFileGuid: String? = ""
)
