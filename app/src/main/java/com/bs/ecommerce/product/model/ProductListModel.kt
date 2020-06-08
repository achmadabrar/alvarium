package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.CategoryModel
import com.bs.ecommerce.product.model.data.Manufacturer
import com.bs.ecommerce.product.model.data.ProductByTagData
import com.bs.ecommerce.product.model.data.ProductByVendorData

interface ProductListModel {

    fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<CategoryModel>
    )

    fun fetchProductsByTag(
        tagId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductByTagData>
    )

    fun fetchProductsByVendor(
        vendorId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductByVendorData>
    )

    fun fetchProductsByManufacturer(
        manufacturerId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<Manufacturer>
    )

    fun applyFilter(
        url: String,
        callback: RequestCompleteListener<CategoryModel>
    )
}