package com.bs.ecommerce.product

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.ProductDetailResponse
import java.util.ArrayList

interface ProductDetailModel {

    fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

    fun addProductToCartModel(keyValueList: ArrayList<KeyValuePair>, callback: RequestCompleteListener<ProductDetailResponse>)

}