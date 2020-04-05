package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.CategoryTreeResponse


class MainViewModel  : BaseViewModel()
{

    var allCategoriesLD = MutableLiveData<List<Category>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()


    var appSettingsLD = MutableLiveData<AppLandingData>()


    fun getCategoryList(model: MainModel)
    {

        isLoadingLD.postValue(true)

        model.getLeftCategories(object : RequestCompleteListener<CategoryTreeResponse>
        {
            override fun onRequestSuccess(data: CategoryTreeResponse)
            {
                isLoadingLD.postValue(false)

                allCategoriesLD.postValue(data.categoryList)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                allCategoriesFailureLD.postValue(listOf())
            }
        })
    }

    fun getAppSettings(model: MainModel)
    {

        isLoadingLD.postValue(true)

        model.getAppLandingSettings(object : RequestCompleteListener<AppLandingSettingResponse>
        {
            override fun onRequestSuccess(data: AppLandingSettingResponse)
            {
                isLoadingLD.postValue(false)

                appSettingsLD.postValue(data.data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

}


