package com.bs.ecommerce.utils

import com.ice.restring.Restring

class SampleStringsLoader : Restring.StringsLoader {

    //This will be called on background thread.
    override fun getLanguages(): List<String?>? {
        val languages = mutableListOf<String>()

        languages.add("en")
        languages.add("ar")

        return languages
    }

    //This will be called on background thread.
    override fun getStrings(language: String?): Map<String?, String?>? {

        val lanMap = mutableMapOf<String?, String?>()

        if (language == "en") {
            lanMap.apply {
                put("home_nav_home", "Home")
                put("home_nav_categories", "Category")
                put("home_nav_search", "Search")
                put("home_nav_account", "Account")
                put("home_nav_more", "More")
            }
        }

        else if (language == "ar") {
            lanMap.apply {
                put("home_nav_home", "والحقوق")
                put("home_nav_categories", "الكرامة")
                put("home_nav_search", "متساوين")
                put("home_nav_account", "جميع")
                put("home_nav_more", "المادة")
            }
        }

        Restring.setStrings(language, lanMap)

        return lanMap
    }

}