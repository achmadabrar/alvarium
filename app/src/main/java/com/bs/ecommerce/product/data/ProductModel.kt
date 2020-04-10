package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

open class ProductModel() : BaseProductModel() {

    @SerializedName("ShortDescription") var shortDescription: String? = null
    @SerializedName("ProductPrice") var productPrice: ProductPrice? = null

    @SerializedName("ReviewOverviewModel") var reviewOverviewModel: ReviewModel? = null

    @SerializedName("StockAvailability") var stockAvailability: String? = null

    //@SerializedName("Quantity") var quantity: Quantity? = null
}