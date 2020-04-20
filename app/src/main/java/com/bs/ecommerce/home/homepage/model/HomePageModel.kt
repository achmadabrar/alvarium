package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.product.model.data.CategoryModel
import com.bs.ecommerce.product.model.data.Manufacturer
import com.google.gson.JsonObject

interface HomePageModel
{
    fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchBestSellingProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchHomePageCategoryList(callback: RequestCompleteListener<List<CategoryModel>>)

    fun fetchManufacturers(callback: RequestCompleteListener<List<Manufacturer>>)

    fun fetchBannerImages(callback: RequestCompleteListener<SliderData>)

}