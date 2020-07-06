package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.AvailableCategory
import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.PagingFilteringContext
import com.bs.ecommerce.catalog.common.ProductSummary
import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("adv") val adv: Boolean?,
    @SerializedName("asv") val asv: Boolean?,
    @SerializedName("AvailableCategories") val availableCategories: List<AvailableCategory>?,
    @SerializedName("AvailableManufacturers") val availableManufacturers: List<AvailableCategory>?,
    @SerializedName("AvailableVendors") val availableVendors: List<AvailableCategory>?,
    @SerializedName("cid") val cid: Int?,
    @SerializedName("CustomProperties") val customProperties: CustomProperties?,
    @SerializedName("isc") val isc: Boolean?,
    @SerializedName("mid") val mid: Int?,
    @SerializedName("NoResults") val noResults: Boolean?,
    @SerializedName("PagingFilteringContext") val pagingFilteringContext: PagingFilteringContext?,
    @SerializedName("pf") val pf: Any?,
    @SerializedName("Products") val products: List<ProductSummary>?,
    @SerializedName("pt") val pt: Any?,
    @SerializedName("q") val q: String?,
    @SerializedName("sid") val sid: Boolean?,
    @SerializedName("vid") val vid: Int?,
    @SerializedName("Warning") val warning: String?
)