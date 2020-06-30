package com.bs.ecommerce.catalog.common


import com.google.gson.annotations.SerializedName

data class PagerModel(
    @SerializedName("CurrentPage")
    val currentPage: Int?,
    @SerializedName("CurrentPageText")
    val currentPageText: String?,
    @SerializedName("FirstButtonText")
    val firstButtonText: String?,
    @SerializedName("IndividualPagesDisplayedCount")
    val individualPagesDisplayedCount: Int?,
    @SerializedName("LastButtonText")
    val lastButtonText: String?,
    @SerializedName("NextButtonText")
    val nextButtonText: String?,
    @SerializedName("PageIndex")
    val pageIndex: Int?,
    @SerializedName("PageSize")
    val pageSize: Int?,
    @SerializedName("PreviousButtonText")
    val previousButtonText: String?,
    @SerializedName("RouteActionName")
    val routeActionName: String?,
    @SerializedName("RouteValues")
    val routeValues: RouteValues?,
    @SerializedName("ShowFirst")
    val showFirst: Boolean?,
    @SerializedName("ShowIndividualPages")
    val showIndividualPages: Boolean?,
    @SerializedName("ShowLast")
    val showLast: Boolean?,
    @SerializedName("ShowNext")
    val showNext: Boolean?,
    @SerializedName("ShowPagerItems")
    val showPagerItems: Boolean?,
    @SerializedName("ShowPrevious")
    val showPrevious: Boolean?,
    @SerializedName("ShowTotalSummary")
    val showTotalSummary: Boolean?,
    @SerializedName("TotalPages")
    val totalPages: Int?,
    @SerializedName("TotalRecords")
    val totalRecords: Int?,
    @SerializedName("UseRouteLinks")
    val useRouteLinks: Boolean?
)