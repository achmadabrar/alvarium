package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import com.google.gson.JsonObject

interface HomePageModel
{
    fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchBestSellingProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchHomePageCategoryList(callback: RequestCompleteListener<List<CategoryModel>>)

    fun fetchManufacturers(callback: RequestCompleteListener<List<Manufacturer>>)

    fun fetchBannerImages(callback: RequestCompleteListener<JsonObject>)

}