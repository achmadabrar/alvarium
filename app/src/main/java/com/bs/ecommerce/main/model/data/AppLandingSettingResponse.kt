package com.bs.ecommerce.main.model.data
import android.os.Parcel
import android.os.Parcelable
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName


data class AppLandingSettingResponse(
    @SerializedName("Data") var data: AppLandingData = AppLandingData()
) : BaseResponse()

data class AppLandingData(
    @SerializedName("AndriodForceUpdate") val andriodForceUpdate: Boolean? = null,
    @SerializedName("AndroidVersion") val androidVersion: String? = null,
    @SerializedName("AppStoreUrl") val appStoreUrl: String? = null,
    @SerializedName("CurrencyNavSelector") var currencyNavSelector: CurrencyNavSelector = CurrencyNavSelector(),
    @SerializedName("IOSForceUpdate") val iOSForceUpdate: Boolean? = null,
    @SerializedName("IOSVersion") val iOSVersion: String? = null,
    @SerializedName("LanguageNavSelector") var languageNavSelector: LanguageNavSelector = LanguageNavSelector(),
    @SerializedName("LogoUrl") val logoUrl: String = "",
    @SerializedName("PlayStoreUrl") val playStoreUrl: String? = null,
    @SerializedName("Rtl") val rtl: Boolean = false,
    @SerializedName("ShowSubCategoryProducts") val showSubCategoryProducts: Boolean = false,
    @SerializedName("ShowAllVendors") val showAllVendors: Boolean? = null,
    @SerializedName("ShowBestsellersOnHomepage") var showBestsellersOnHomepage: Boolean? = null,
    @SerializedName("ShowFeaturedProducts") var showFeaturedProducts: Boolean? = null,
    @SerializedName("ShowHomepageCategoryProducts") var showHomepageCategoryProducts: Boolean? = null,
    @SerializedName("ShowHomepageSlider") var showHomepageSlider: Boolean? = null,
    @SerializedName("ShowManufacturers") var showManufacturers: Boolean? = null,
    @SerializedName("TotalShoppingCartProducts") val totalShoppingCartProducts: Int = 0,
    @SerializedName("TotalWishListProducts") val totalWishListProducts: Int = 0,
    @SerializedName("AnonymousCheckoutAllowed") val anonymousCheckoutAllowed: Boolean = false,
    @SerializedName("ShowChangeBaseUrlPanel") val showChangeBaseUrlPanel: Boolean = false,
    @SerializedName("HasReturnRequests") val hasReturnRequests: Boolean = false,
    @SerializedName("StringResources") var stringResources: List<StringResource> = listOf()
)

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
