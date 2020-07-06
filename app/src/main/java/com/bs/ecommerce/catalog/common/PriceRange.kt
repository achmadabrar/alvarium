package com.bs.ecommerce.catalog.common


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PriceRange(
    @SerializedName("FilterUrl")
    val filterUrl: String?,
    @SerializedName("From")
    val from: String?,
    @SerializedName("Selected")
    val selected: Boolean = false,
    @SerializedName("To")
    val to: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readString(),
        parcel.readParcelable(CustomProperties::class.java.classLoader)
    ) {
    }

    override fun toString(): String {
        return if (from == null && to != null) "Under $to"
        else if (from != null && to == null) "Over $from"
        else if (from != null && to != null) "$from - $to"
        else "--"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filterUrl)
        parcel.writeString(from)
        parcel.writeValue(selected)
        parcel.writeString(to)
        parcel.writeParcelable(customProperties, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceRange> {
        override fun createFromParcel(parcel: Parcel): PriceRange {
            return PriceRange(parcel)
        }

        override fun newArray(size: Int): Array<PriceRange?> {
            return arrayOfNulls(size)
        }
    }
}