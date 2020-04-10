package com.bs.ecommerce.product.data

import com.bs.ecommerce.networking.BaseResponse
import com.google.gson.annotations.SerializedName

class ProductDetailResponse : BaseResponse() {

    @SerializedName("Data") var data: ProductDetail? = null
}
