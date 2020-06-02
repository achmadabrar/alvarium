package com.bs.ecommerce.db

class DbHelper {

    companion object {

        var currentLanguageId: Int = 1

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

    }
}