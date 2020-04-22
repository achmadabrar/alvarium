package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SpecificationFilter(
    @SerializedName("AlreadyFilteredItems")
    val alreadyFilteredItems: List<FilterItems>? = listOf(),
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Enabled")
    val enabled: Boolean? = false,
    @SerializedName("NotFilteredItems")
    val notFilteredItems: List<FilterItems>? = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(FilterItems),
        parcel.readParcelable(CustomProperties::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createTypedArrayList(FilterItems)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(alreadyFilteredItems)
        parcel.writeParcelable(customProperties, flags)
        parcel.writeValue(enabled)
        parcel.writeTypedList(notFilteredItems)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpecificationFilter> {
        override fun createFromParcel(parcel: Parcel): SpecificationFilter {
            return SpecificationFilter(parcel)
        }

        override fun newArray(size: Int): Array<SpecificationFilter?> {
            return arrayOfNulls(size)
        }
    }
}