package com.bs.ecommerce.product

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import java.util.HashMap

class ProductListViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<CategoryModel>()
    var manufacturerLD = MutableLiveData<Manufacturer>()

    var toastMessageLD = MutableLiveData("")
    var pageNumberLD = MutableLiveData(1)

    private var queryMapLD = MutableLiveData<MutableMap<String, String>>()

    fun getProductByCategory(catId: Long, model: ProductListModel) {
        isLoadingLD.postValue(true)

        model.fetchProducts(catId, getQueryMap(), object : RequestCompleteListener<CategoryModel> {
            override fun onRequestSuccess(data: CategoryModel) {
                isLoadingLD.postValue(false)

                productLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
                toastMessageLD.postValue(errorMessage)
            }

        })
    }

    fun getProductByManufacturer(manufacturerId: Long, model: ProductListModel) {
        isLoadingLD.postValue(true)

        model.fetchProductsByManufacturer(
            manufacturerId,
            mapOf(),
            object : RequestCompleteListener<Manufacturer> {
                override fun onRequestSuccess(data: Manufacturer) {
                    isLoadingLD.postValue(false)
                    manufacturerLD.postValue(data)
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