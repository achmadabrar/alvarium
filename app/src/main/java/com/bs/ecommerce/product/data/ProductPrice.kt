package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/9/2015.
 */

data class ProductPrice(
        @SerializedName("OldPrice") val oldPrice: String,
        @SerializedName("OldPriceValue") val oldPriceValue: Float? = null,

        @SerializedName("Price") val price: String,
        @SerializedName("PriceValue") val priceValue: Float? = null
)

/*class ProductPrice() {
    var oldPrice: String? = null

    var price: String? = null
}*/
