package com.bs.ecommerce.db

import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.StringResource

class DbHelper {

    companion object {

        var currentLanguageId: Int = 1

        //var memCache: Map<String, StrResource> = mutableMapOf()
        var memCache: AppLandingData? = null

        fun isLanguageLoaded(languageId: Int): Boolean {

            val temp = AppDatabase.getInstance().isLanguageDownloaded(languageId)

            return temp != null
        }


        fun getString(key: String): String {
            val temp = AppDatabase.getInstance().getString(key)

            return temp?.value ?: key
        }
        fun getStringWithNumber(key: String, number: Int): String = getString(key).replace("{0}", number.toString())

        fun getStringWithNumber(key: String, number: String): String = getString(key).replace("{0}", number)

        fun addLanguage(stringResources: List<StringResource>, id: Int) {

            val temp = mutableListOf<StrResource>()

            AppDatabase.getInstance().deleteAllStrings()

            for(i in stringResources) {
                temp.add(StrResource(i.key, i.value, id))
            }

            currentLanguageId = id

            AppDatabase.getInstance().insertAll(temp)
        }
    }
}