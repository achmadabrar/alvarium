package com.bs.ecommerce.product.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import java.util.ArrayList

interface ProductDetailModel {

    fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

    fun addProductToCartModel(keyValueList: ArrayList<KeyValuePair>, callback: RequestCompleteListener<ProductDetailResponse>)

}