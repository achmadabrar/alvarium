package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PriceRangeFilter(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Enabled")
    val enabled: Boolean? = false,
    @SerializedName("Items")
    val items: List<PriceRange>? = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CustomProperties::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createTypedArrayList(PriceRange)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(customProperties, flags)
        parcel.writeValue(enabled)
        parcel.writeTypedList(items)
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