package com.bs.ecommerce.main.model.data
import android.os.Parcel
import android.os.Parcelable
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.product.model.data.StringResource
import com.google.gson.annotations.SerializedName


data class AppLandingSettingResponse(
    @SerializedName("Data") var data: AppLandingData = AppLandingData()
) : BaseResponse()

data class AppLandingData(
    @SerializedName("AndriodForceUpdate") val andriodForceUpdate: Boolean? = null,
    @SerializedName("AndroidVersion") val androidVersion: String? = null,
    @SerializedName("AppStoreUrl") val appStoreUrl: String? = null,
    @SerializedName("CurrencyNavSelector") var currencyNavSelector: CurrencyNavSelector = CurrencyNavSelector(),
    @SerializedName("EnableBestSellingProducts") var enableBestSellingProducts: Boolean = false,
    @SerializedName("EnableFeatureProducts") var enableFeatureProducts: Boolean = false,
    @SerializedName("EnableHomeCategoriesProducts") var enableHomeCategoriesProducts: Boolean = false,
    @SerializedName("EnableHomePageSlider") var enableHomePageSlider: Boolean = false,
    @SerializedName("EnableSubCategoriesProducts") var enableSubCategoriesProducts: Boolean = false,
    @SerializedName("IOSForceUpdate") val iOSForceUpdate: Boolean? = null,
    @SerializedName("IOSVersion") val iOSVersion: String? = null,
    @SerializedName("LanguageNavSelector") var languageNavSelector: LanguageNavSelector = LanguageNavSelector(),
    @SerializedName("LogoUrl") val logoUrl: String = "",
    @SerializedName("PlayStoreUrl") val playStoreUrl: String? = null,
    @SerializedName("Rtl") val rtl: Boolean = false,
    @SerializedName("ShowSubCategoryProducts") val showSubCategoryProducts: Boolean = false,
    @SerializedName("ShowAllVendors") val showAllVendors: Boolean? = null,
    @SerializedName("TotalShoppingCartProducts") val totalShoppingCartProducts: Int = 0,
    @SerializedName("StringResources") var stringResources: List<StringResource> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(CurrencyNavSelector::class.java.classLoader) ?: CurrencyNavSelector(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readParcelable(LanguageNavSelector::class.java.classLoader) ?: LanguageNavSelector(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt(),
        parcel.createTypedArrayList(StringResource.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(andriodForceUpdate)
        parcel.writeString(androidVersion)
        parcel.writeString(appStoreUrl)
        parcel.writeParcelable(currencyNavSelector, flags)
        parcel.writeByte(if (enableBestSellingProducts) 1 else 0)
        parcel.writeByte(if (enableFeatureProducts) 1 else 0)
        parcel.writeByte(if (enableHomeCategoriesProducts) 1 else 0)
        parcel.writeByte(if (enableHomePageSlider) 1 else 0)
        parcel.writeByte(if (enableSubCategoriesProducts) 1 else 0)
        parcel.writeValue(iOSForceUpdate)
        parcel.writeString(iOSVersion)
        parcel.writeParcelable(languageNavSelector, flags)
        parcel.writeString(logoUrl)
        parcel.writeString(playStoreUrl)
        parcel.writeByte(if (rtl) 1 else 0)
        parcel.writeByte(if (showSubCategoryProducts) 1 else 0)
        parcel.writeValue(showAllVendors)
        parcel.writeInt(totalShoppingCartProducts)
        parcel.writeTypedList(stringResources)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppLandingData> {
        override fun createFromParcel(parcel: Parcel): AppLandingData {
            return AppLandingData(parcel)
        }

        override fun newArray(size: Int): Array<AppLandingData?> {
            return arrayOfNulls(size)
        }
    }
}

data class CurrencyNavSelector(
    @SerializedName("AvailableCurrencies") var availableCurrencies: List<AvailableCurrency> = listOf(),
    @SerializedName("CurrentCurrencyId") var currentCurrencyId: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(AvailableCurrency) ?: listOf(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(availableCurrencies)
        parcel.writeInt(currentCurrencyId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyNavSelector> {
        override fun createFromParcel(parcel: Parcel): CurrencyNavSelector {
            return CurrencyNavSelector(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyNavSelector?> {
            return arrayOfNulls(size)
        }
    }
}

data class LanguageNavSelector(
    @SerializedName("AvailableLanguages") var availableLanguages: List<AvailableLanguage> = listOf(),
    @SerializedName("CurrentLanguageId") var currentLanguageId: Int = 0,
    @SerializedName("UseImages") var useImages: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(AvailableLanguage) ?: listOf(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(availableLanguages)
        parcel.writeInt(currentLanguageId)
        parcel.writeByte(if (useImages) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LanguageNavSelector> {
        override fun createFromParcel(parcel: Parcel): LanguageNavSelector {
            return LanguageNavSelector(parcel)
        }

        override fun newArray(size: Int): Array<LanguageNavSelector?> {
            return arrayOfNulls(size)
        }
    }
}

data class AvailableCurrency(
    @SerializedName("CurrencySymbol") var currencySymbol: String = "",
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("Name") var name: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currencySymbol)
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableCurrency> {
        override fun createFromParcel(parcel: Parcel): AvailableCurrency {
            return AvailableCurrency(parcel)
        }

        override fun newArray(size: Int): Array<AvailableCurrency?> {
            return arrayOfNulls(size)
        }
    }
}


data class AvailableLanguage(
    @SerializedName("FlagImageFileName") var flagImageFileName: String = "",
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("Name") var name: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(flagImageFileName)
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableLanguage> {
        override fun createFromParcel(parcel: Parcel): AvailableLanguage {
            return AvailableLanguage(parcel)
        }

        override fun newArray(size: Int): Array<AvailableLanguage?> {
            return arrayOfNulls(size)
        }
    }
}
