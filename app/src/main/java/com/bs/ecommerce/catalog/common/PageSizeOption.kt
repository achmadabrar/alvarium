package com.bs.ecommerce.catalog.common


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PageSizeOption(
    @SerializedName("Disabled")
    val disabled: Boolean? = false,
    @SerializedName("Selected")
    val selected: Boolean? = false,
    @SerializedName("Text")
    val text: String? = "",
    @SerializedName("Value")
    val value: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(disabled)
        parcel.writeValue(selected)
        parcel.writeString(text)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageSizeOption> {
        override fun createFromParcel(parcel: Parcel): PageSizeOption {
            return PageSizeOption(parcel)
        }

        override fun newArray(size: Int): Array<PageSizeOption?> {
            return arrayOfNulls(size)
        }
    }
}