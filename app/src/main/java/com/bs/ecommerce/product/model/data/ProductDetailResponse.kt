package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

class ProductDetailResponse : BaseResponse() {

    @SerializedName("Data") var data: ProductDetail? = null
}
