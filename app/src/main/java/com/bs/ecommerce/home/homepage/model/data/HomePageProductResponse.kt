package com.bs.ecommerce.home.homepage.model.data

import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.product.model.data.ProductSummary
import com.google.gson.annotations.SerializedName


data class HomePageProductResponse(
    @SerializedName("Data") var homePageProductList: List<ProductSummary> = listOf()
) : BaseResponse()
