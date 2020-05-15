package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.product.model.data.CategoryModel
import com.bs.ecommerce.product.model.data.Manufacturer
import com.bs.ecommerce.product.model.data.ProductSummary


class MainViewModel : BaseViewModel() {

    var navDrawerCategoriesLD = MutableLiveData<List<Category>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()

    var bestSellingProductLD = MutableLiveData<List<ProductSummary>>()
    var featuredProductListLD = MutableLiveData<List<ProductSummary>>()
    var manufacturerListLD = MutableLiveData<List<Manufacturer>>()
    var featuredCategoryLD = MutableLiveData<List<CategoryModel>>()
    var imageBannerLD = MutableLiveData<SliderData>()

    var appSettingsLD = MutableLiveData<AppLandingData>()
    var toastMessageLD = MutableLiveData<String>()

    var testUrlSuccessLD = MutableLiveData<Boolean>()


    fun getFeaturedProducts(model: HomePageModel) {

        isLoadingLD.value = true

        model.getFeaturedProducts(object : RequestCompleteListener<HomePageProductResponse> {
            override fun onRequestSuccess(data: HomePageProductResponse) {
                isLoadingLD.value = false

                featuredProductListLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                allCategoriesFailureLD.value = listOf()
            }
        })
    }

    fun getCategoryListWithProducts(model: HomePageModel) {
        model.fetchHomePageCategoryList(object : RequestCompleteListener<List<CategoryModel>> {
            override fun onRequestSuccess(data: List<CategoryModel>) {
                featuredCategoryLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                featuredCategoryLD.value = listOf()
                toastMessageLD.value = errorMessage
            }
        })
    }

    fun getBestSellingProducts(model: HomePageModel) {
        model.fetchBestSellingProducts(object : RequestCompleteListener<HomePageProductResponse> {
            override fun onRequestSuccess(data: HomePageProductResponse) {
                bestSellingProductLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                bestSellingProductLD.value = listOf()
                toastMessageLD.value = errorMessage
            }

        })
    }

    fun getManufactures(model: HomePageModel) {
        model.fetchManufacturers(object : RequestCompleteListener<List<Manufacturer>> {
            override fun onRequestSuccess(data: List<Manufacturer>) {
                manufacturerListLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                manufacturerListLD.value = listOf()
                toastMessageLD.value = errorMessage
            }
        })
    }

    fun getBannerImages(model: HomePageModel) {
        model.fetchBannerImages(object : RequestCompleteListener<SliderData> {
            override fun onRequestSuccess(data: SliderData) {
                imageBannerLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                toastMessageLD.value = errorMessage
            }

        })
    }

    fun getNavDrawerCategoryList(model: MainModel) {
        isLoadingLD.value = true

        model.getLeftCategories(object : RequestCompleteListener<CategoryTreeResponse> {
            override fun onRequestSuccess(data: CategoryTreeResponse) {
                isLoadingLD.value = false

                navDrawerCategoriesLD.value = data.categoryList

                testUrlSuccessLD.value = true
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false

                allCategoriesFailureLD.value = listOf()

                testUrlSuccessLD.value = false
            }
        })
    }

    fun getAppSettings(model: MainModel) {

        isLoadingLD.value = true

        model.getAppLandingSettings(object : RequestCompleteListener<AppLandingSettingResponse> {
            override fun onRequestSuccess(data: AppLandingSettingResponse) {
                isLoadingLD.value = false

                appSettingsLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }


    fun changeLanguage(languageId : Int, model: MainModel) {

        isLoadingLD.value = true

        model.changeLanguage(languageId.toLong(), object : RequestCompleteListener<BaseResponse> {
            override fun onRequestSuccess(data: BaseResponse) {
                isLoadingLD.value = false

                toastMessageLD.value = data.message
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toastMessageLD.value = errorMessage
            }
        })
    }

    fun changeCurrency(currencyId : Int, model: MainModel) {

        isLoadingLD.value = true

        model.changeCurrency(currencyId.toLong(), object : RequestCompleteListener<BaseResponse> {
            override fun onRequestSuccess(data: BaseResponse) {
                isLoadingLD.value = false

                toastMessageLD.value = data.message
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toastMessageLD.value = errorMessage
            }
        })
    }

}


