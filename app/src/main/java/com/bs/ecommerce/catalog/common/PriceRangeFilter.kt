package com.bs.ecommerce.catalog.common


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PriceRangeFilter(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Enabled")
    val enabled: Boolean? = false,
    @SerializedName("Items")
    val items: List<PriceRange>? = listOf(),
    @SerializedName("RemoveFilterUrl")
    val removeFilterUrl: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CustomProperties::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createTypedArrayList(PriceRange),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(customProperties, flags)
        parcel.writeValue(enabled)
        parcel.writeTypedList(items)
        parcel.writeString(removeFilterUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceRangeFilter> {
        override fun createFromParcel(parcel: Parcel): PriceRangeFilter {
            return PriceRangeFilter(parcel)
        }

        override fun newArray(size: Int): Array<PriceRangeFilter?> {
            return arrayOfNulls(size)
        }
    }
}