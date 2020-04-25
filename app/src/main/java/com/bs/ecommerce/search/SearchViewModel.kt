package com.bs.ecommerce.search

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.model.data.SearchResult
import com.bs.ecommerce.search.model.SearchModel
import com.bs.ecommerce.search.model.SearchParam
import java.util.*

class SearchViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<SearchResult>()

    var toastMessageLD = MutableLiveData("")
    var pageNumberLD = MutableLiveData(1)

    private var queryMapLD = MutableLiveData<MutableMap<String, String>>()

    fun searchProduct(query: String, model: SearchModel) {

        val searchParam = SearchParam(1)

        isLoadingLD.postValue(true)

        model.searchProducts(query, searchParam, object : RequestCompleteListener<SearchResult> {
            override fun onRequestSuccess(data: SearchResult) {
                isLoadingLD.postValue(false)

                productLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
                toastMessageLD.postValue(errorMessage)
            }

        })
    }

    private fun getQueryMap(): MutableMap<String, String> {
        val map = queryMapLD.value ?: HashMap<String, String>()
        map[Api.qs_page_number] = pageNumberLD.value.toString()
        map[Api.qs_page_size] = 10.toString()

        queryMapLD.postValue(map)

        return map
    }
}