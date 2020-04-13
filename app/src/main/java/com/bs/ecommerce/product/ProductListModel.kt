package com.bs.ecommerce.product

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.ProductResponseData

interface ProductListModel {

    fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductResponseData>
    )

    fun fetchProductsByManufacturer(
        manufacturerId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductResponseData>
    )
}