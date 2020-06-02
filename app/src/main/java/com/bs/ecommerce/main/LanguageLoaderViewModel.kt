package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.AppDatabase
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.db.StrResource
import com.bs.ecommerce.home.homepage.model.StringResourceModelImpl
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.product.model.data.StringResourceResponse
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.showLog

class LanguageLoaderViewModel: BaseViewModel() {

    var isLanguageLoaded = MutableLiveData<Boolean>()
    var showLoader = MutableLiveData<Boolean>()

    fun downloadLanguage(languageId: Int?) {
        showLoader.value = true

        if(languageId!=null) {
            getLanguageResourceById(languageId)
        } else {
            getLanguageIdFromAppSettings()
        }
    }

    private fun getLanguageIdFromAppSettings() {

        MainModelImpl(MyApplication.mAppContext!!).getAppLandingSettings(
            object : RequestCompleteListener<AppLandingSettingResponse> {

                override fun onRequestSuccess(data: AppLandingSettingResponse) {

                    val id = data.data.languageNavSelector.currentLanguageId
                    "lang_".showLog("Language ID: $id")

                    getLanguageResourceById(id)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLanguageLoaded.value = false
                    showLoader.value = false
                }
            })
    }

    private fun getLanguageResourceById(id: Int) {

        StringResourceModelImpl().getStringResource(
            id,
            object : RequestCompleteListener<StringResourceResponse> {
                override fun onRequestSuccess(data: StringResourceResponse) {
                    // TODO save to local DB. delete previous one
                    // val temp = mutableListOf<StrResource>()
                    AppDatabase.getInstance().deleteAllStrings()

                    for(i in data.stringResource) {
                        //temp.add(StrResource(i.key, i.value, id))
                        AppDatabase.getInstance().insertString(
                            StrResource(
                                i.key,
                                i.value,
                                id
                            )
                        )
                    }

                    DbHelper.currentLanguageId = id

                    // AppDatabase.getInstance().stringDao().insertAll(temp)

                    isLanguageLoaded.value = true
                    showLoader.value = false
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLanguageLoaded.value = false
                    showLoader.value = false
                }

            })
    }
}