package com.bs.ecommerce.more.downloadableProducts.model.data
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class DownloadableProductListResponse(@SerializedName("Data") var downloadableProductList: DownloadableProductList? = DownloadableProductList()) : BaseResponse()

data class DownloadableProductList(@SerializedName("Items") var items: List<DownloadableProductItem>? = listOf())

data class DownloadableProductItem(
    @SerializedName("CreatedOn") var createdOn: String? = "",
    @SerializedName("CustomOrderNumber") var customOrderNumber: String? = "",
    @SerializedName("DownloadId") var downloadId: Int? = 0,
    @SerializedName("LicenseId") var licenseId: Int? = 0,
    @SerializedName("OrderId") var orderId: Int = 0,
    @SerializedName("OrderItemGuid") var orderItemGuid: String? = "",
    @SerializedName("ProductAttributes") var productAttributes: String? = "",
    @SerializedName("ProductId") var productId: Int = 0,
    @SerializedName("ProductName") var productName: String? = "",
    @SerializedName("ProductSeName") var productSeName: String? = ""
)

