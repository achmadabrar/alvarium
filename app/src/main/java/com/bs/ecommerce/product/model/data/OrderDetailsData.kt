package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class OrderDetailsData(
    @SerializedName("BillingAddress")
    val billingAddress: Address?,
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
    val items: List<Item>?,
    @SerializedName("OrderNotes")
    val orderNotes: List<String>?,
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
    val pickupAddress: Address?,
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
    val shipments: List<Any>?,
    @SerializedName("ShippingAddress")
    val shippingAddress: Address?,
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