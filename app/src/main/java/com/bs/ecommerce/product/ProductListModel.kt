package com.bs.ecommerce.product

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.CategoryModel

interface ProductListModel {

    fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<CategoryModel>
    )

    fun fetchProductsByManufacturer(
        manufacturerId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<CategoryModel>
    )
}