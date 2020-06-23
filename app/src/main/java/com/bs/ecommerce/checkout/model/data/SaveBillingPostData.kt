package com.bs.ecommerce.checkout.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class SaveBillingPostData(@SerializedName("Data") var data: BillingAddress = BillingAddress()) :
    BaseResponse()

data class SaveShippingPostData(@SerializedName("Data") var data: ShippingAddress = ShippingAddress()) :
    BaseResponse()


data class ShippingAddress(@SerializedName("ShippingNewAddress") var shippingNewAddress: AddressModel = AddressModel())