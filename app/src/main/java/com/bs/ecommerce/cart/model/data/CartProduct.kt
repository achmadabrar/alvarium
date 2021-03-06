package com.bs.ecommerce.cart.model.data

import com.google.gson.annotations.SerializedName
import java.util.*

class CartProduct
{
    @SerializedName("Sku") var sku: Any? = null

    @SerializedName("ProductId") var productId: Long? = null

    @SerializedName("ProductName") var productName: String? = null

    @SerializedName("ProductSeName") var productSeName: String? = null

    @SerializedName("UnitPrice") var unitPrice: String? = null

    @SerializedName("SubTotal") var subTotal: String? = null

    @SerializedName("Discount") var discount: Any? = null

    @SerializedName("Quantity") var quantity: Int = 0

    @SerializedName("AllowedQuantities") var allowedQuantities: List<Any> = ArrayList()

    @SerializedName("AttributeInfo") var attributeInfo: String = "null"

    @SerializedName("IsAllowItemEditing") var isAllowItemEditing: Boolean? = null

    @SerializedName("Warnings") var warnings: List<String>? = null

    @SerializedName("Id") var id: Int? = null

    @SerializedName("Picture") var picture: PictureModel? = null

}


