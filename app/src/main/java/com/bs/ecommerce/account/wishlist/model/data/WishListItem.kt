package com.bs.ecommerce.account.wishlist.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.PictureModel
import com.google.gson.annotations.SerializedName

data class WishListItem(
    @SerializedName("AllowItemEditing")
    val allowItemEditing: Boolean?,
    @SerializedName("AllowedQuantities")
    val allowedQuantities: List<Any>?,
    @SerializedName("AttributeInfo")
    val attributeInfo: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Discount")
    val discount: Any?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("MaximumDiscountedQty")
    val maximumDiscountedQty: Any?,
    @SerializedName("Picture")
    val picture: PictureModel?,
    @SerializedName("ProductId")
    val productId: Long?,
    @SerializedName("ProductName")
    val productName: String?,
    @SerializedName("ProductSeName")
    val productSeName: String?,
    @SerializedName("Quantity")
    val quantity: Int?,
    @SerializedName("RecurringInfo")
    val recurringInfo: Any?,
    @SerializedName("RentalInfo")
    val rentalInfo: Any?,
    @SerializedName("Sku")
    val sku: String?,
    @SerializedName("SubTotal")
    val subTotal: String?,
    @SerializedName("UnitPrice")
    val unitPrice: String?,
    @SerializedName("Warnings")
    val warnings: List<String>?
)