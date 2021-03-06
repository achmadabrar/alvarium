package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("CategoryBreadcrumb")
    val categoryBreadcrumb: List<CategoryBreadcrumb>? = listOf(),
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("DisplayCategoryBreadcrumb")
    val displayCategoryBreadcrumb: Boolean? = false,
    @SerializedName("FeaturedProducts")
    val featuredProducts: List<ProductSummary>? = listOf(),
    @SerializedName("Id")
    val id: Int? = 0,
    @SerializedName("Name")
    val name: String? = "",
    @SerializedName("PagingFilteringContext")
    val pagingFilteringContext: PagingFilteringContext? = PagingFilteringContext(),
    @SerializedName("PictureModel")
    val pictureModel: PictureModel? = PictureModel(),
    @SerializedName("Products")
    val products: List<ProductSummary>? = listOf(),
    @SerializedName("SeName")
    val seName: String? = "",
    @SerializedName("SubCategories")
    val subCategories: List<SubCategory>? = listOf()
)