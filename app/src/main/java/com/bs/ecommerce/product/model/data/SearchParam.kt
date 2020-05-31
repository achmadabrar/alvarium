package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.utils.PrefSingleton
import java.util.*

data class SearchParam (
    var pageNumber: Int = 0,
    var pageSize: Int = PrefSingleton.getPageSize(),
    var orderBy: Int? = null,
    var viewMode: Int? = null,
    var queryMap: HashMap<String, String> = hashMapOf()
) {

    fun incrementPageNumber() {
        ++pageNumber
    }

    fun clear() {
        pageNumber = 0
        pageSize = PrefSingleton.getPageSize()
        orderBy = null
        viewMode = null
        queryMap.clear()
    }
}