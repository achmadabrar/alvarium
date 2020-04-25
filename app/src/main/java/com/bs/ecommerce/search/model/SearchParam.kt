package com.bs.ecommerce.search.model

data class SearchParam (
    var pageNumber: Int = 1,
    var pageSize: Int = 9,
    var orderBy: Int? = null,
    var viewMode: Int? = null
)