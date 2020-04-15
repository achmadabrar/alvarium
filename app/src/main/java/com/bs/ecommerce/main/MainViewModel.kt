package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.data.HomePageProduct
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import com.google.gson.JsonObject


class MainViewModel : BaseViewModel() {

    var navDrawerCategoriesLD = MutableLiveData<List<Category>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()

    var featuredProductListLD = MutableLiveData<List<HomePageProduct>>()
    var manufacturerListLD = MutableLiveData<List<Manufacturer>>()
    var featuredCategoryLD = MutableLiveData<List<CategoryModel>>()

    var appSettingsLD = MutableLiveData<AppLandingData>()
    var toastMessageLD = MutableLiveData<String>()


    fun getFeaturedProducts(model: HomePageModel) {

        isLoadingLD.postValue(true)

        model.getFeaturedProducts(object : RequestCompleteListener<HomePageProductResponse> {
            override fun onRequestSuccess(data: HomePageProductResponse) {
                isLoadingLD.postValue(false)

                featuredProductListLD.postValue(data.homePageProductList)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
                allCategoriesFailureLD.postValue(listOf())
            }
        })
    }

    fun getCategoryListWithProducts(model: HomePageModel) {
        model.fetchHomePageCategoryList(object : RequestCompleteListener<List<CategoryModel>> {
            override fun onRequestSuccess(data: List<CategoryModel>) {
                featuredCategoryLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                featuredCategoryLD.postValue(listOf())
                toastMessageLD.postValue(errorMessage)
            }
        })
    }

    fun getBestSellingProducts(model: HomePageModel) {
        // TODO implement
    }

    fun getManufactures(model: HomePageModel) {
        model.fetchManufacturers(object : RequestCompleteListener<List<Manufacturer>> {
            override fun onRequestSuccess(data: List<Manufacturer>) {
                manufacturerListLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                manufacturerListLD.postValue(listOf())
                toastMessageLD.postValue(errorMessage)
            }
        })
    }

    fun getBannerImages(model: HomePageModel) {
        model.fetchBannerImages(object : RequestCompleteListener<JsonObject> {
            override fun onRequestSuccess(data: JsonObject) {

            }

            override fun onRequestFailed(errorMessage: String) {
                toastMessageLD.postValue(errorMessage)
            }

        })
    }

    fun getNavDrawerCategoryList(model: MainModel) {
        isLoadingLD.postValue(true)

        model.getLeftCategories(object : RequestCompleteListener<CategoryTreeResponse> {
            override fun onRequestSuccess(data: CategoryTreeResponse) {
                isLoadingLD.postValue(false)

                navDrawerCategoriesLD.postValue(data.categoryList)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
                allCategoriesFailureLD.postValue(listOf())
            }
        })
    }

    fun getAppSettings(model: MainModel) {

        isLoadingLD.postValue(true)

        model.getAppLandingSettings(object : RequestCompleteListener<AppLandingSettingResponse> {
            override fun onRequestSuccess(data: AppLandingSettingResponse) {
                isLoadingLD.postValue(false)

                appSettingsLD.postValue(data.data)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }
        })
    }

}


