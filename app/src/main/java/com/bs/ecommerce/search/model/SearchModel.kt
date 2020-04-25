package com.bs.ecommerce.search.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.SearchResult

interface SearchModel {

    fun searchProducts(
        query: String,
        searchParam: SearchParam,
        callback: RequestCompleteListener<SearchResult>
    )
}