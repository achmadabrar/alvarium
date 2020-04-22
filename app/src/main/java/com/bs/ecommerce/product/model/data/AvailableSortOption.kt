package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AvailableSortOption(
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

    companion object CREATOR : Parcelable.Creator<AvailableSortOption> {
        override fun createFromParcel(parcel: Parcel): AvailableSortOption {
            return AvailableSortOption(parcel)
        }

        override fun newArray(size: Int): Array<AvailableSortOption?> {
            return arrayOfNulls(size)
        }
    }
}