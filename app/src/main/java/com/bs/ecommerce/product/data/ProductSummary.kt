package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

data class ProductSummary(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DefaultPictureModel")
    val defaultPictureModel: DefaultPictureModel?,
    @SerializedName("FullDescription")
    val fullDescription: String?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("MarkAsNew")
    val markAsNew: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("ProductPrice")
    val productPrice: ProductPrice?,
    @SerializedName("ProductType")
    val productType: Int?,
    @SerializedName("ReviewOverviewModel")
    val reviewOverviewModel: ReviewOverviewModel?,
    @SerializedName("SeName")
    val seName: String?,
    @SerializedName("ShortDescription")
    val shortDescription: String?,
    @SerializedName("Sku")
    val sku: String?,
    @SerializedName("SpecificationAttributeModels")
    val specificationAttributeModels: List<Any>?
)