package com.bs.ecommerce.main

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.checkout.CheckoutViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.data.*
import com.bs.ecommerce.more.model.TopicModel
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.product.model.data.*
import com.bs.ecommerce.utils.OneTimeEvent
import com.bs.ecommerce.utils.showLog
import java.util.concurrent.atomic.AtomicInteger


class MainViewModel : CheckoutViewModel() {

    var navDrawerCategoriesLD = MutableLiveData<List<Category>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()

    var bestSellingProductLD = MutableLiveData<List<ProductSummary>>()
    var featuredProductListLD = MutableLiveData<List<ProductSummary>>()
    var manufacturerListLD = MutableLiveData<List<Manufacturer>>()
    var featuredCategoryLD = MutableLiveData<List<CategoryModel>>()
    var imageBannerLD = MutableLiveData<SliderData>()

    var appSettingsLD = MutableLiveData<OneTimeEvent<AppLandingData?>>()
    var appStartResponseLD = MutableLiveData<Boolean>()
    var showLoader = MutableLiveData<OneTimeEvent<Boolean>>()

    var testUrlSuccessLD = MutableLiveData<Boolean>()

    var languageChangeSuccessLD = MutableLiveData<Boolean>()
    var currencyChangeSuccessLD = MutableLiveData<Boolean>()

    private val logTag: String = "nop_" + this::class.java.simpleName
    private var numberOfApis  = 0
    private var count: AtomicInteger = AtomicInteger(0)
    var homePageLoader = MutableLiveData<Boolean>()

    var topicLD = MutableLiveData<TopicData?>()

    var updatingAppSettings = false

    fun getAllLandingPageProducts(model: HomePageModel) {

        Log.d("nop_", "called getAllLandingPageProducts ${count.get()}")

        numberOfApis = 0
        homePageLoader.value = true

        val appLandingData = appSettingsLD.value?.peekContent()

        if(appLandingData == null) {
            numberOfApis = 5

            getFeaturedProducts(model)
            getCategoryListWithProducts(model)
            getManufactures(model)
            getBannerImages(model)
            getBestSellingProducts(model)
        } else {
            appLandingData.apply {
                if(enableFeatureProducts) {
                    getFeaturedProducts(model)
                    numberOfApis++
                }

                if(enableHomeCategoriesProducts) {
                    getCategoryListWithProducts(model)
                    numberOfApis++
                }

                if(enableHomePageSlider) {
                    getBannerImages(model)
                    numberOfApis++
                }

                if(enableBestSellingProducts) {
                    getBestSellingProducts(model)
                    numberOfApis++
                }

                getManufactures(model)
                numberOfApis++
            }
        }
    }

    private fun hideLoader() {
        val serviceCalled = count.incrementAndGet()

        if(serviceCalled == numberOfApis) {
            homePageLoader.value = false
            count = AtomicInteger(0)
        }
    }

    fun fetchTopic(sysName: String, model: TopicModel) {

        isLoadingLD.value = true

        model.getTopicBySystemName(sysName, object: RequestCompleteListener<TopicResponse> {

            override fun onRequestSuccess(data: TopicResponse) {
                isLoadingLD.value = false

                if(data.data != null)
                    topicLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }


    fun getFeaturedProducts(model: HomePageModel) {

        model.getFeaturedProducts(object : RequestCompleteListener<HomePageProductResponse> {
            override fun onRequestSuccess(data: HomePageProductResponse) {
                hideLoader()
                featuredProductListLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                hideLoader()
                allCategoriesFailureLD.value = listOf()
            }
        })
    }

    fun getCategoryListWithProducts(model: HomePageModel) {
        model.fetchHomePageCategoryList(object : RequestCompleteListener<List<CategoryModel>> {
            override fun onRequestSuccess(data: List<CategoryModel>) {
                featuredCategoryLD.value = data
                hideLoader()
            }

            override fun onRequestFailed(errorMessage: String) {
                featuredCategoryLD.value = listOf()
                toast(errorMessage)
                hideLoader()
            }
        })
    }

    fun getBestSellingProducts(model: HomePageModel) {
        model.fetchBestSellingProducts(object : RequestCompleteListener<HomePageProductResponse> {
            override fun onRequestSuccess(data: HomePageProductResponse) {
                hideLoader()
                bestSellingProductLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                hideLoader()
                bestSellingProductLD.value = listOf()
                logTag.showLog(errorMessage)
            }

        })
    }

    fun getManufactures(model: HomePageModel) {
        model.fetchManufacturers(object : RequestCompleteListener<List<Manufacturer>> {
            override fun onRequestSuccess(data: List<Manufacturer>) {
                hideLoader()
                manufacturerListLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                hideLoader()
                manufacturerListLD.value = listOf()
                logTag.showLog(errorMessage)
            }
        })
    }

    fun getBannerImages(model: HomePageModel) {
        model.fetchBannerImages(object : RequestCompleteListener<SliderData> {
            override fun onRequestSuccess(data: SliderData) {
                hideLoader()
                imageBannerLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                hideLoader()
                logTag.showLog(errorMessage)
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

    fun setAppSettings(data: AppLandingData) {
        appSettingsLD.value = OneTimeEvent(data)
    }

    fun getAppSettings(model: MainModel, saveLanguage: Boolean = true) {

        isLoadingLD.value = true

        model.getAppLandingSettings(
            object : RequestCompleteListener<AppLandingSettingResponse> {

                override fun onRequestSuccess(data: AppLandingSettingResponse) {

                    isLoadingLD.value = false

                    val id = data.data.languageNavSelector.currentLanguageId
                    "lang_".showLog("Language ID: $id")

                    //getLanguageResourceById(id)

                    if(saveLanguage) {
                        DbHelper.addLanguage(data.data.stringResources, id)
                    }

                    appSettingsLD.value = OneTimeEvent(data.data)
                    showLoader.value = OneTimeEvent(false)
                }

                override fun onRequestFailed(errorMessage: String) {
                    appSettingsLD.value = OneTimeEvent(null)
                    showLoader.value = OneTimeEvent(false)
                    isLoadingLD.value = false

                    toast(errorMessage)
                }
            })
    }



    fun changeLanguage(languageId : Int, model: MainModel) {

        isLoadingLD.value = true

        model.changeLanguage(languageId.toLong(), object : RequestCompleteListener<BaseResponse> {
            override fun onRequestSuccess(data: BaseResponse) {
                isLoadingLD.value = false

                languageChangeSuccessLD.postValue(true)
                toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

    fun changeCurrency(currencyId : Int, model: MainModel) {

        isLoadingLD.value = true

        model.changeCurrency(currencyId.toLong(), object : RequestCompleteListener<BaseResponse> {
            override fun onRequestSuccess(data: BaseResponse) {
                isLoadingLD.value = false

                currencyChangeSuccessLD.postValue(true)
                toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

    fun isUpdateNeeded(context: Context, it: AppLandingData): Boolean {

        if(it.andriodForceUpdate == true
            && !it.playStoreUrl.isNullOrEmpty()
            && !it.androidVersion.isNullOrEmpty()) {

            try {
                val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                val installedVersionName = pInfo.versionName

                val v1 = installedVersionName.split(".")
                val v2 = it.androidVersion!!.split(".")

                return when {
                    v2[0].toInt() > v1[0].toInt() -> {
                        true
                    }
                    v2[1].toInt() > v1[1].toInt() -> {
                        true
                    }
                    else -> v2[2].toInt() > v1[2].toInt()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } else {
            return false
        }
    }

    fun submitAppStart(appStartRequest: AppStartRequest, model: MainModel) {

        isLoadingLD.value = true

        model.submitAppStart(appStartRequest, object : RequestCompleteListener<BaseResponse> {
            override fun onRequestSuccess(data: BaseResponse) {
                isLoadingLD.value = false

                appStartResponseLD.postValue(true)
                toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

}


