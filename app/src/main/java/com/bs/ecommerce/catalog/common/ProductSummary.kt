package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class ProductSummary(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = null,
    @SerializedName("DefaultPictureModel")
    val defaultPictureModel: DefaultPictureModel? = null,
    @SerializedName("FullDescription")
    val fullDescription: String? = null,
    @SerializedName("MarkAsNew")
    val markAsNew: Boolean? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("ProductPrice")
    val productPrice: ProductSummaryPrice? = null,
    @SerializedName("ProductType")
    val productType: Int? = null,
    @SerializedName("ReviewOverviewModel")
    val reviewOverviewModel: ReviewOverviewModel? = null,
    @SerializedName("SeName")
    val seName: String? = null,
    @SerializedName("ShortDescription")
    val shortDescription: String? = null,
    @SerializedName("Sku")
    val sku: String? = null,
    @SerializedName("SpecificationAttributeModels")
    val specificationAttributeModels: List<Any>? = null
)