package com.bs.ecommerce.search.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.SearchResponse
import com.bs.ecommerce.product.model.data.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchModelImpl : SearchModel {

    override fun searchProducts(
        query: String,
        searchParam: SearchParam,
        callback: RequestCompleteListener<SearchResult>
    ) {
        RetroClient.api.searchProduct(
            searchParam.pageNumber,
            searchParam.pageSize,
            searchParam.orderBy.toString(),
            searchParam.viewMode.toString(),
            query
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
                    callback.onRequestFailed(response.message())
            }
        })
    }
}