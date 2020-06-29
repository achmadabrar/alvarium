package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserAgreementResponse(
    @SerializedName("Data")
    val `data`: UserAgreementData?
): BaseResponse()

data class UserAgreementData(
    @SerializedName("OrderItemGuid")
    val orderItemGuid: String?,
    @SerializedName("UserAgreementText")
    val userAgreementText: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderItemGuid)
        parcel.writeString(userAgreementText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAgreementData> {
        override fun createFromParcel(parcel: Parcel): UserAgreementData {
            return UserAgreementData(parcel)
        }

        override fun newArray(size: Int): Array<UserAgreementData?> {
            return arrayOfNulls(size)
        }
    }
}