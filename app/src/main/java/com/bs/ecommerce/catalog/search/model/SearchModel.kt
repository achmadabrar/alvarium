package com.bs.ecommerce.catalog.search.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.catalog.common.SearchResult
import com.bs.ecommerce.catalog.common.SearchParam

interface SearchModel {

    fun searchProducts(
        searchParam: SearchParam,
        callback: RequestCompleteListener<SearchResult>
    )
    fun getModelsForAdvancedSearch(callback: RequestCompleteListener<SearchResult>)
}