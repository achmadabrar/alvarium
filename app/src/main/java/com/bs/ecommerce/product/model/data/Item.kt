package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("AttributeInfo")
    val attributeInfo: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DownloadId")
    val downloadId: Int?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("LicenseId")
    val licenseId: Int?,
    @SerializedName("OrderItemGuid")
    val orderItemGuid: String?,
    @SerializedName("ProductId")
    val productId: Int?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("Quantity")
    val quantity: Int?,
    @SerializedName("RentalInfo")
    val rentalInfo: Any?,
    @SerializedName("Sku")
    val sku: String?,
    @SerializedName("SubTotal")
    val subTotal: String?,
    @SerializedName("UnitPrice")
    val unitPrice: String?,
    @SerializedName("VendorName")
    val vendorName: String?
)