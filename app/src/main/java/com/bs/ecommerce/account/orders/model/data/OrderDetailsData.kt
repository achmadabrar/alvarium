package com.bs.ecommerce.account.orders.model.data


import com.bs.ecommerce.cart.model.data.GiftCard
import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.CustomValues
import com.bs.ecommerce.catalog.common.TaxRate
import com.google.gson.annotations.SerializedName

data class OrderDetailsData(
    @SerializedName("BillingAddress")
    val billingAddress: AddressModel?,
    @SerializedName("CanRePostProcessPayment")
    val canRePostProcessPayment: Boolean?,
    @SerializedName("CheckoutAttributeInfo")
    val checkoutAttributeInfo: String?,
    @SerializedName("CreatedOn")
    val createdOn: String?,
    @SerializedName("CustomOrderNumber")
    val customOrderNumber: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("CustomValues")
    val customValues: CustomValues?,
    @SerializedName("DisplayTax")
    val displayTax: Boolean?,
    @SerializedName("DisplayTaxRates")
    val displayTaxRates: Boolean?,
    @SerializedName("DisplayTaxShippingInfo")
    val displayTaxShippingInfo: Boolean?,
    @SerializedName("GiftCards")
    val giftCards: List<GiftCard>?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IsReOrderAllowed")
    val isReOrderAllowed: Boolean?,
    @SerializedName("IsReturnRequestAllowed")
    val isReturnRequestAllowed: Boolean?,
    @SerializedName("IsShippable")
    val isShippable: Boolean?,
    @SerializedName("Items")
    val items: List<OrderItem>?,
    @SerializedName("OrderNotes")
    val orderNotes: List<OrderNotes>?,
    @SerializedName("OrderShipping")
    val orderShipping: String?,
    @SerializedName("OrderStatus")
    val orderStatus: String?,
    @SerializedName("OrderSubTotalDiscount")
    val orderSubTotalDiscount: String?,
    @SerializedName("OrderSubtotal")
    val orderSubtotal: String?,
    @SerializedName("OrderTotal")
    val orderTotal: String?,
    @SerializedName("OrderTotalDiscount")
    val orderTotalDiscount: String?,
    @SerializedName("PaymentMethod")
    val paymentMethod: String?,
    @SerializedName("PaymentMethodAdditionalFee")
    val paymentMethodAdditionalFee: Any?,
    @SerializedName("PaymentMethodStatus")
    val paymentMethodStatus: String?,
    @SerializedName("PdfInvoiceDisabled")
    val pdfInvoiceDisabled: Boolean?,
    @SerializedName("PickupAddress")
    val pickupAddress: AddressModel?,
    @SerializedName("PickupInStore")
    val pickupInStore: Boolean?,
    @SerializedName("PricesIncludeTax")
    val pricesIncludeTax: Boolean?,
    @SerializedName("PrintMode")
    val printMode: Boolean?,
    @SerializedName("RedeemedRewardPoints")
    val redeemedRewardPoints: Int?,
    @SerializedName("RedeemedRewardPointsAmount")
    val redeemedRewardPointsAmount: Any?,
    @SerializedName("Shipments")
    val shipments: List<ShipmentItem>,
    @SerializedName("ShippingAddress")
    val shippingAddress: AddressModel?,
    @SerializedName("ShippingMethod")
    val shippingMethod: String?,
    @SerializedName("ShippingStatus")
    val shippingStatus: String?,
    @SerializedName("ShowSku")
    val showSku: Boolean?,
    @SerializedName("ShowVendorName")
    val showVendorName: Boolean?,
    @SerializedName("Tax")
    val tax: String?,
    @SerializedName("TaxRates")
    val taxRates: List<TaxRate>?,
    @SerializedName("VatNumber")
    val vatNumber: Any?
)



data class ShipmentItem(
    @SerializedName("DeliveryDate") var deliveryDate: String? = "",
    @SerializedName("Id") var id: Int? = 0,
    @SerializedName("ShippedDate") var shippedDate: String? = "",
    @SerializedName("TrackingNumber") var trackingNumber: String? = ""
)