package com.bs.ecommerce.checkout.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class CartProduct
{
    @SerializedName("Sku") var sku: Any? = null

    @SerializedName("ProductId") var productId: Int? = null

    @SerializedName("ProductName") var productName: String? = null

    @SerializedName("ProductSeName") var productSeName: String? = null

    @SerializedName("UnitPrice") var unitPrice: String? = null

    @SerializedName("SubTotal") var subTotal: String? = null

    @SerializedName("Discount") var discount: Any? = null

    @SerializedName("Quantity") var quantity: Int? = null

    @SerializedName("AllowedQuantities") var allowedQuantities: List<Any> = ArrayList()

    @SerializedName("AttributeInfo") var attributeInfo: String? = null

    @SerializedName("IsAllowItemEditing") var isAllowItemEditing: Boolean? = null

    @SerializedName("Warnings") var warnings: List<Any>? = null

    @SerializedName("Id") var id: Int? = null

    @SerializedName("Picture") var picture: PictureModel? = null

}


