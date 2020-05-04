package com.bs.ecommerce.checkout.model.data

import com.google.gson.annotations.SerializedName

data class SaveBillingPostData(@SerializedName("Data") var data: BillingAddress = BillingAddress())
