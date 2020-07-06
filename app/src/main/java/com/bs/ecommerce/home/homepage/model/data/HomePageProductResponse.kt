package com.bs.ecommerce.home.homepage.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.catalog.common.ProductSummary
import com.google.gson.annotations.SerializedName


data class HomePageProductResponse(
    @SerializedName("Data") var homePageProductList: List<ProductSummary>?
) : BaseResponse()
