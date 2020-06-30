package com.bs.ecommerce.catalog.common

import com.bs.ecommerce.catalog.common.ProductDetail
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

class ProductDetailResponse : BaseResponse() {

    @SerializedName("Data") var data: ProductDetail? = null
}
