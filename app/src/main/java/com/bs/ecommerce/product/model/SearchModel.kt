package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.SearchResult
import com.bs.ecommerce.product.model.data.SearchParam

interface SearchModel {

    fun searchProducts(
        searchParam: SearchParam,
        callback: RequestCompleteListener<SearchResult>
    )
}