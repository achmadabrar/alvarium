package com.bs.ecommerce.product

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.ProductDetailResponse

interface ProductDetailModel {

    fun getProductDetail(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

}