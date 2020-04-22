package com.bs.ecommerce.product.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.data.*
import java.util.HashMap

class ProductListViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<CategoryModel>()
    var manufacturerLD = MutableLiveData<Manufacturer>()

    var toastMessageLD = MutableLiveData("")
    var pageNumberLD = MutableLiveData(1)

    var filterAttributeLD = MutableLiveData<MutableMap<String, MutableList<FilterItems>>>()
    var priceRangeLD = MutableLiveData<List<PriceRange>>()

    var filterVisibilityLD = MutableLiveData<Boolean>()

    private var queryMapLD = MutableLiveData<MutableMap<String, String>>()

    fun getProductByCategory(catId: Long, model: ProductListModel) {
        isLoadingLD.value = true

        model.fetchProducts(catId, getQueryMap(), object : RequestCompleteListener<CategoryModel> {
            override fun onRequestSuccess(data: CategoryModel) {
                isLoadingLD.value = false

                productLiveData.value = data

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toastMessageLD.value = errorMessage
            }

        })
    }

    fun getProductByManufacturer(manufacturerId: Long, model: ProductListModel) {
        isLoadingLD.value = true

        model.fetchProductsByManufacturer(
            manufacturerId,
            mapOf(),
            object : RequestCompleteListener<Manufacturer> {
                override fun onRequestSuccess(data: Manufacturer) {
                    isLoadingLD.value = false
                    manufacturerLD.value = data

                    preparePriceFilter(data.pagingFilteringContext)
                    prepareFilterAttribute(data.pagingFilteringContext)
                    setFilterVisibility(data.pagingFilteringContext)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.value = false
                    toastMessageLD.value = errorMessage
                }
            })
    }

    fun applyFilter(filterUrl: String?, model: ProductListModel) {
        if (filterUrl.isNullOrEmpty()) return

        // Strip base URL from full path
        val endPoint = filterUrl.replace(Constants.BASE_URL, "")

        model.applyFilter(endPoint, object : RequestCompleteListener<CategoryModel> {
            override fun onRequestSuccess(data: CategoryModel) {
                isLoadingLD.value = false

                productLiveData.value = data

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toastMessageLD.value = errorMessage
            }

        })
    }

    private fun getQueryMap(): MutableMap<String, String> {
        val map = queryMapLD.value ?: HashMap<String, String>()
        map[Api.qs_page_number] = pageNumberLD.value.toString()
        map[Api.qs_page_size] = 10.toString()

        queryMapLD.value = map

        return map
    }

    private fun prepareFilterAttribute(filterInfo: PagingFilteringContext?) {
        val notFilteredItems = filterInfo?.specificationFilter?.notFilteredItems

        val mMap: MutableMap<String, MutableList<FilterItems>> = mutableMapOf()

        if (!notFilteredItems.isNullOrEmpty()) {

            for (i in notFilteredItems) {

                if (mMap.containsKey(i.specificationAttributeName)) {
                    mMap[i.specificationAttributeName]?.add(i)
                } else {
                    val tmp = mutableListOf<FilterItems>()
                    tmp.add(i)
                    mMap[i.specificationAttributeName ?: ""] = tmp
                }
            }
        }

        filterAttributeLD.value = mMap
    }

    private fun preparePriceFilter(filterInfo: PagingFilteringContext?) {
        if (filterInfo?.priceRangeFilter?.enabled == true && filterInfo.priceRangeFilter.items != null) {
            priceRangeLD.value = filterInfo.priceRangeFilter.items
        } else {
            priceRangeLD.value = mutableListOf()
        }
    }

    private fun setFilterVisibility(filterInfo: PagingFilteringContext?) {
        val hasPrice =
            filterInfo?.priceRangeFilter?.enabled == true && filterInfo.priceRangeFilter.items != null

        val hasFilter = !filterInfo?.specificationFilter?.notFilteredItems.isNullOrEmpty()

        val hasAlreadyFiltered =
            !filterInfo?.specificationFilter?.alreadyFilteredItems.isNullOrEmpty()

        filterVisibilityLD.value = hasPrice || hasFilter || hasAlreadyFiltered
    }
}