package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.networking.Api
import java.util.HashMap

data class SearchParam (
    var pageNumber: Int = 0,
    var pageSize: Int = Api.DEFAULT_PAGE_SIZE,
    var orderBy: Int? = null,
    var viewMode: Int? = null,
    var queryMap: HashMap<String, String> = hashMapOf()
) {

    fun incrementPageNumber() {
        ++pageNumber
    }

    fun clear() {
        pageNumber = 0
        pageSize = Api.DEFAULT_PAGE_SIZE
        orderBy = null
        viewMode = null
        queryMap.clear()
    }
}