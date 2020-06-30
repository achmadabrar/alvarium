package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.catalog.common.CategoryModel
import com.bs.ecommerce.home.homepage.model.data.Manufacturer

interface HomePageModel
{
    fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchBestSellingProducts(callback: RequestCompleteListener<HomePageProductResponse>)

    fun fetchHomePageCategoryList(callback: RequestCompleteListener<List<CategoryModel>>)

    fun fetchManufacturers(callback: RequestCompleteListener<List<Manufacturer>>)

    fun fetchBannerImages(callback: RequestCompleteListener<SliderData>)

}