package com.bs.ecommerce.catalog.common


import android.os.Parcel
import android.os.Parcelable

class CustomProperties(
) : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomProperties> {
        override fun createFromParcel(parcel: Parcel): CustomProperties {
            return CustomProperties(parcel)
        }

        override fun newArray(size: Int): Array<CustomProperties?> {
            return arrayOfNulls(size)
        }
    }
}