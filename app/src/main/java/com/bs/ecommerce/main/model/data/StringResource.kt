package com.bs.ecommerce.main.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class StringResource(
    @SerializedName("Key")
    val key: String,
    @SerializedName("Value")
    val value: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(key)
        writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StringResource> =
            object : Parcelable.Creator<StringResource> {
                override fun createFromParcel(source: Parcel): StringResource =
                    StringResource(source)

                override fun newArray(size: Int): Array<StringResource?> = arrayOfNulls(size)
            }
    }
}