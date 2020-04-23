package com.bs.ecommerce.product.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.Constants
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.data.*
import java.util.*

class ProductListViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<CategoryModel>()
    var manufacturerLD = MutableLiveData<Manufacturer>()

    var applicableFilterLD = MutableLiveData<MutableMap<String, MutableList<FilterItems>>>()
    var appliedFilterLD = MutableLiveData<MutableMap<String, MutableList<FilterItems>>>()
    var priceRangeLD = MutableLiveData<PriceRangeFilter?>()

    var toastMessageLD = MutableLiveData("")
    var filterVisibilityLD = MutableLiveData<Boolean>()

    private var pageNumber = 0
    var shouldAppend = false

    private var queryMapLD = MutableLiveData<MutableMap<String, String>>()

    fun getProductByCategory(catId: Long, model: ProductListModel) {

        if(isLoadingLD.value == true || productLiveData.value?.pagingFilteringContext?.hasNextPage == false ) {
            Log.d("nop_", "rejected")
            return
        } else {
            Log.d("nop_", "calling")
        }

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
        var endPoint = filterUrl.replace(Constants.BASE_URL, "")

        // reset page number
        endPoint = endPoint.replace("${Api.qs_page_number}=$pageNumber", "${Api.qs_page_number}=1")
        pageNumber = 1

        shouldAppend = false

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
        // calculate next page to load
        val currentPage = productLiveData.value?.pagingFilteringContext?.pageNumber ?: 0
        pageNumber = currentPage + 1

        // whether or not the new data should be added with existing data
        shouldAppend = currentPage>=1

        val map = queryMapLD.value ?: HashMap<String, String>()
        map[Api.qs_page_number] = pageNumber.toString()
        map[Api.qs_page_size] = 9.toString()

        queryMapLD.value = map

        return map
    }

    private fun prepareFilterAttribute(filterInfo: PagingFilteringContext?) {
        // Applicable Filter
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

        applicableFilterLD.value = mMap

        // Already applied filter
        val appliedFilteredItems = filterInfo?.specificationFilter?.alreadyFilteredItems

        mMap.clear()

        if (!appliedFilteredItems.isNullOrEmpty()) {

            for (i in appliedFilteredItems) {

                // Adding URL to clear filter as each items filterUrl - bad practice
                i.filterUrl = filterInfo.specificationFilter.removeFilterUrl

                if (mMap.containsKey(i.specificationAttributeName)) {
                    mMap[i.specificationAttributeName]?.add(i)
                } else {
                    val tmp = mutableListOf<FilterItems>()
                    tmp.add(i)
                    mMap[i.specificationAttributeName ?: ""] = tmp
                }
            }
        }

        appliedFilterLD.value = mMap
    }

    private fun preparePriceFilter(filterInfo: PagingFilteringContext?) {
        if (filterInfo?.priceRangeFilter?.enabled == true && filterInfo.priceRangeFilter.items != null) {
            priceRangeLD.value = filterInfo.priceRangeFilter
        } else {
            priceRangeLD.value = null
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