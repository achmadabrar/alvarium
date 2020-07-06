package com.bs.ecommerce.catalog.search.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.catalog.common.SearchParam
import com.bs.ecommerce.catalog.common.SearchResponse
import com.bs.ecommerce.catalog.common.SearchResult
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchModelImpl : SearchModel {

    override fun searchProducts(
        searchParam: SearchParam,
        callback: RequestCompleteListener<SearchResult>
    ) {
        RetroClient.api.searchProduct(
            searchParam.pageNumber,
            searchParam.pageSize,
            searchParam.queryMap
            ).enqueue(object : Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as SearchResult)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }
        })
    }
    override fun getModelsForAdvancedSearch(callback: RequestCompleteListener<SearchResult>)
    {
        RetroClient.api.getModelsForAdvancedSearch().enqueue(object : Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as SearchResult)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }
        })
    }
}