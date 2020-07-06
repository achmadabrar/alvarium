package com.bs.ecommerce.account.orders.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class ShipmentDetailsResponse(@SerializedName("Data") var data: ShipmentDetailsData? = ShipmentDetailsData()) : BaseResponse()

data class ShipmentDetailsData(
    @SerializedName("DeliveryDate") var deliveryDate: String? = "",
    @SerializedName("Id") var id: Int? = 0,
    @SerializedName("Items") var items: List<ShippedItem>? = listOf(),
    @SerializedName("Order") var order: OrderDetailsData? = null,
    @SerializedName("ShipmentStatusEvents") var shipmentStatusEvents: List<Any>? = listOf(),
    @SerializedName("ShippedDate") var shippedDate: String? = "",
    @SerializedName("ShowSku") var showSku: Boolean? = false,
    @SerializedName("TrackingNumber") var trackingNumber: String? = "",
    @SerializedName("TrackingNumberUrl") var trackingNumberUrl: Any? = Any()
)



data class ShippedItem(
    @SerializedName("AttributeInfo") var attributeInfo: String? = "",
    @SerializedName("Id") var id: Int? = 0,
    @SerializedName("ProductId") var productId: Int? = 0,
    @SerializedName("ProductName") var productName: String? = "",
    @SerializedName("ProductSeName") var productSeName: String? = "",
    @SerializedName("QuantityOrdered") var quantityOrdered: Int? = 0,
    @SerializedName("QuantityShipped") var quantityShipped: Int? = 0,
    @SerializedName("RentalInfo") var rentalInfo: Any? = Any(),
    @SerializedName("Sku") var sku: String? = ""
)