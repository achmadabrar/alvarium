package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class PagingFilteringContext(
    @SerializedName("AllowCustomersToSelectPageSize")
    val allowCustomersToSelectPageSize: Boolean? = false,
    @SerializedName("AllowProductSorting")
    val allowProductSorting: Boolean? = false,
    @SerializedName("AllowProductViewModeChanging")
    val allowProductViewModeChanging: Boolean? = false,
    @SerializedName("AvailableSortOptions")
    val availableSortOptions: List<AvailableSortOption>? = listOf(),
    @SerializedName("AvailableViewModes")
    val availableViewModes: List<AvailableViewMode>? = listOf(),
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("FirstItem")
    val firstItem: Int? = 0,
    @SerializedName("HasNextPage")
    val hasNextPage: Boolean? = false,
    @SerializedName("HasPreviousPage")
    val hasPreviousPage: Boolean? = false,
    @SerializedName("LastItem")
    val lastItem: Int? = 0,
    @SerializedName("PageIndex")
    val pageIndex: Int? = 0,
    @SerializedName("PageNumber")
    val pageNumber: Int? = 0,
    @SerializedName("PageSize")
    val pageSize: Int? = 0,
    @SerializedName("PageSizeOptions")
    val pageSizeOptions: List<PageSizeOption>? = listOf(),
    @SerializedName("PriceRangeFilter")
    val priceRangeFilter: PriceRangeFilter? = PriceRangeFilter(),
    @SerializedName("SpecificationFilter")
    val specificationFilter: SpecificationFilter? = SpecificationFilter(),
    @SerializedName("TotalItems")
    val totalItems: Int? = 0,
    @SerializedName("TotalPages")
    val totalPages: Int? = 0,
    @SerializedName("ViewMode")
    val viewMode: String? = ""
)