package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

data class ProductByVendorData (
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("FeaturedProducts")
    val featuredProducts: List<ProductSummary>?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("MetaDescription")
    val metaDescription: String?,
    @SerializedName("MetaKeywords")
    val metaKeywords: List<String>?,
    @SerializedName("MetaTitle")
    val metaTitle: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("PagingFilteringContext")
    val pagingFilteringContext: PagingFilteringContext?,
    @SerializedName("PictureModel")
    val pictureModel: PictureModel?,
    @SerializedName("Products")
    val products: List<ProductSummary>?,
    @SerializedName("SeName")
    val seName: String?,
    @SerializedName("AllowCustomersToContactVendors")
    val allowCustomersToContactVendors: Boolean?
)