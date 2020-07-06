package com.bs.ecommerce.catalog.productList.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.catalog.common.CategoryModel
import com.bs.ecommerce.home.homepage.model.data.Manufacturer
import com.bs.ecommerce.catalog.common.ProductByTagData
import com.bs.ecommerce.catalog.common.ProductByVendorData

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