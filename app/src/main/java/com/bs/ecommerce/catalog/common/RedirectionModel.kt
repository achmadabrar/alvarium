package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class RedirectionModel(
    @SerializedName("RedirectToDetailsPage")
    val redirectToDetailsPage: Boolean?,
    @SerializedName("RedirectToShoppingCartPage")
    val redirectToShoppingCartPage: Boolean?,
    @SerializedName("RedirectToWishListPage")
    val redirectToWishListPage: Boolean?,
    @SerializedName("TotalShoppingCartProducts")
    val totalShoppingCartProducts: Int?,
    @SerializedName("TotalWishListProducts")
    val totalWishListProducts: Int?
)