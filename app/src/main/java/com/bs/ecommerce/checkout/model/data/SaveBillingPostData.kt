package com.bs.ecommerce.checkout.model.data

import com.google.gson.annotations.SerializedName

data class SaveBillingPostData(@SerializedName("Data") var data: BillingAddress = BillingAddress())

data class SaveShippingPostData(@SerializedName("Data") var data: ShippingAddress = ShippingAddress())


data class ShippingAddress(@SerializedName("ShippingNewAddress") var shippingNewAddress: AddressModel = AddressModel())