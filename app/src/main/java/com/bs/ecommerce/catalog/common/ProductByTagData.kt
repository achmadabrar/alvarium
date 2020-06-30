package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

data class ProductByTagData (
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("TagName")
    val name: String?,
    @SerializedName("PagingFilteringContext")
    val pagingFilteringContext: PagingFilteringContext?,
    @SerializedName("Products")
    val products: List<ProductSummary>?,
    @SerializedName("TagSeName")
    val seName: String?
)