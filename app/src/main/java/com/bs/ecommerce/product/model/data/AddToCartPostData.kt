package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName

/*data class AddToCartPostData(
    @SerializedName("Data") var data: Any = Any(),
    @SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf(),
    @SerializedName("UploadPicture") var uploadPicture: Any = Any()
)*/

data class UpdateCartPostData(@SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf())