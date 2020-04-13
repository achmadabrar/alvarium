package com.bs.ecommerce.product

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.data.ProductResponse
import com.bs.ecommerce.product.data.ProductResponseData
import com.google.gson.JsonObject
import java.util.HashMap

class ProductListViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<ProductResponseData>()
    var toastMessageLD = MutableLiveData("")
    var pageNumberLD = MutableLiveData(1)
    var queryMapLD = MutableLiveData<MutableMap<String, String>>()

    fun getProductDetail(catId: Long, model: ProductListModel) {
        isLoadingLD.postValue(true)

        model.fetchProducts(catId, getQueryMap(), object : RequestCompleteListener<ProductResponseData> {
            override fun onRequestSuccess(data: ProductResponseData) {
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