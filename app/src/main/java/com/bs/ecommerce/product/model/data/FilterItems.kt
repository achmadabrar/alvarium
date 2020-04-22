package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FilterItems(
    @SerializedName("FilterUrl")
    val filterUrl: String?,
    @SerializedName("SpecificationAttributeName")
    val specificationAttributeName: String? = "",
    @SerializedName("SpecificationAttributeOptionColorRgb")
    val specificationAttributeOptionColorRgb: String?,
    @SerializedName("SpecificationAttributeOptionName")
    val specificationAttributeOptionName: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(filterUrl)
        parcel.writeString(specificationAttributeName)
        parcel.writeString(specificationAttributeOptionColorRgb)
        parcel.writeString(specificationAttributeOptionName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterItems> {
        override fun createFromParcel(parcel: Parcel): FilterItems {
            return FilterItems(parcel)
        }

        override fun newArray(size: Int): Array<FilterItems?> {
            return arrayOfNulls(size)
        }
    }
}