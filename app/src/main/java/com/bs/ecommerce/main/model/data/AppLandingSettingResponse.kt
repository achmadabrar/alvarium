package com.bs.ecommerce.main.model.data
import com.google.gson.annotations.SerializedName


data class AppLandingSettingResponse(
    @SerializedName("Data") var data: AppLandingData = AppLandingData(),
    @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
    @SerializedName("Message") var message: Any = Any()
)

data class AppLandingData(
    @SerializedName("CurrencyNavSelector") var currencyNavSelector: CurrencyNavSelector = CurrencyNavSelector(),
    @SerializedName("EnableBestSellingProducts") var enableBestSellingProducts: Boolean = false,
    @SerializedName("EnableFeatureProducts") var enableFeatureProducts: Boolean = false,
    @SerializedName("EnableHomeCategoriesProducts") var enableHomeCategoriesProducts: Boolean = false,
    @SerializedName("EnableHomePageSlider") var enableHomePageSlider: Boolean = false,
    @SerializedName("EnableSubCategoriesProducts") var enableSubCategoriesProducts: Boolean = false,
    @SerializedName("LanguageNavSelector") var languageNavSelector: LanguageNavSelector = LanguageNavSelector(),
    @SerializedName("NumberOfHomeCategoriesProducts") var numberOfHomeCategoriesProducts: Int = 0,
    @SerializedName("NumberOfManufaturer") var numberOfManufaturer: Int = 0
)

data class CurrencyNavSelector(
    @SerializedName("AvailableCurrencies") var availableCurrencies: List<AvailableCurrency> = listOf(),
    @SerializedName("CurrentCurrencyId") var currentCurrencyId: Int = 0
)

data class LanguageNavSelector(
    @SerializedName("AvailableLanguages") var availableLanguages: List<AvailableLanguage> = listOf(),
    @SerializedName("CurrentLanguageId") var currentLanguageId: Int = 0,
    @SerializedName("UseImages") var useImages: Boolean = false
)

data class AvailableCurrency(
    @SerializedName("CurrencySymbol") var currencySymbol: String = "",
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("Name") var name: String = ""
)



data class AvailableLanguage(
    @SerializedName("FlagImageFileName") var flagImageFileName: String = "",
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("Name") var name: String = ""
)
