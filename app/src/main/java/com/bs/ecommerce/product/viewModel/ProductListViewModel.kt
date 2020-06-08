package com.bs.ecommerce.product.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.model.ProductListModel
import com.bs.ecommerce.product.model.SearchModel
import com.bs.ecommerce.product.model.data.*
import com.bs.ecommerce.utils.PrefSingleton
import java.util.*

class ProductListViewModel : BaseViewModel() {
    var productLiveData = MutableLiveData<CategoryModel>()
    var tagLiveData = MutableLiveData<ProductByTagData>()
    var vendorLiveData = MutableLiveData<ProductByVendorData>()
    var searchResultLD = MutableLiveData<SearchResult>()
    var manufacturerLD = MutableLiveData<Manufacturer>()

    var applicableFilterLD = MutableLiveData<MutableMap<String, MutableList<FilterItems>>>()
    var appliedFilterLD = MutableLiveData<MutableMap<String, MutableList<FilterItems>>>()
    var priceRangeLD = MutableLiveData<PriceRangeFilter?>()

    var filterVisibilityLD = MutableLiveData<Boolean>()

    private var pageNumber = 0
    var shouldAppend = false

    private var queryMapLD = MutableLiveData<MutableMap<String, String>>()
    private var searchParam = SearchParam()

    fun getProductByCategory(catId: Long, resetFilters: Boolean, model: ProductListModel) {

        // don't call if already loading
        if (isLoadingLD.value == true || productLiveData.value?.pagingFilteringContext?.hasNextPage == false) {
            return
        }

        isLoadingLD.value = true

        model.fetchProducts(catId, getQueryMap(1, resetFilters), object : RequestCompleteListener<CategoryModel> {
            override fun onRequestSuccess(data: CategoryModel) {
                isLoadingLD.value = false

                productLiveData.value = data

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getProductByTag(tagId: Long, resetFilters: Boolean, model: ProductListModel) {

        // don't call if already loading
        if (isLoadingLD.value == true || tagLiveData.value?.pagingFilteringContext?.hasNextPage == false) {
            return
        }

        isLoadingLD.value = true

        model.fetchProductsByTag(tagId, getQueryMap(3, resetFilters), object : RequestCompleteListener<ProductByTagData> {
            override fun onRequestSuccess(data: ProductByTagData) {
                isLoadingLD.value = false

                tagLiveData.value = data

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getProductByVendor(vendorId: Long, resetFilters: Boolean, model: ProductListModel) {

        // don't call if already loading
        if (isLoadingLD.value == true || vendorLiveData.value?.pagingFilteringContext?.hasNextPage == false) {
            return
        }

        isLoadingLD.value = true

        model.fetchProductsByVendor(vendorId, getQueryMap(4, resetFilters), object : RequestCompleteListener<ProductByVendorData> {
            override fun onRequestSuccess(data: ProductByVendorData) {
                isLoadingLD.value = false

                vendorLiveData.value = data

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }


    fun getProductByManufacturer(manufacturerId: Long, resetFilters: Boolean, model: ProductListModel) {
        // don't call if already loading
        if (isLoadingLD.value == true || manufacturerLD.value?.pagingFilteringContext?.hasNextPage == false) {
            return
        }

        isLoadingLD.value = true

        model.fetchProductsByManufacturer(
            manufacturerId,
            getQueryMap(2, resetFilters),
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
                    toast(errorMessage)
                }
            })
    }

    fun searchProduct(query: String, resetFilters: Boolean, model: SearchModel) {

        if (isLoadingLD.value == true) {
            return
        }

        if(!resetFilters && searchResultLD.value?.pagingFilteringContext?.hasNextPage == false) {
            return
        }

        shouldAppend = !resetFilters

        searchParam.apply {
            if(resetFilters) clear()
            incrementPageNumber()
            queryMap["q"] = query
        }

        search(model)
    }

    fun applySearchFilter(filterUrl: String?, model: SearchModel) {
        if (filterUrl.isNullOrEmpty()) return
        if (isLoadingLD.value == true) return

        searchParam.apply {
            clear()
            incrementPageNumber()
        }

        shouldAppend = false

        val uri = Uri.parse(filterUrl)
        val paramNames = uri.queryParameterNames
        for(param in paramNames) {
            val value = uri.getQueryParameter(param)
            Log.d("nop_", "$param > $value")

            searchParam.queryMap[param] = value.toString()
        }

        if(searchParam.queryMap.containsKey(Api.qs_order_by))
            searchParam.orderBy = searchParam.queryMap[Api.qs_order_by]?.toInt()

        search(model)
    }

    private fun search(model: SearchModel) {
        isLoadingLD.postValue(true)

        model.searchProducts(searchParam, object : RequestCompleteListener<SearchResult> {
            override fun onRequestSuccess(data: SearchResult) {
                isLoadingLD.postValue(false)
                searchResultLD.postValue(data)

                preparePriceFilter(data.pagingFilteringContext)
                prepareFilterAttribute(data.pagingFilteringContext)
                setFilterVisibility(data.pagingFilteringContext)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
                toast(errorMessage)
            }

        })
    }

    fun applyFilter(catId: Long, filterUrl: String?, model: ProductListModel, type: Int) {

        if (filterUrl.isNullOrEmpty()) return
        if (isLoadingLD.value == true) return

        isLoadingLD.value = true

        val map = /*queryMapLD.value ?:*/ HashMap<String, String>()


        val uri = Uri.parse(filterUrl)
        val paramNames = uri.queryParameterNames
        for(param in paramNames) {
            val value = uri.getQueryParameter(param)
            Log.d("nop_", "$param > $value")

            map[param] = value.toString()
        }

        map[Api.qs_page_number] = 1.toString()
        map[Api.qs_page_size] = PrefSingleton.getPageSize().toString()

        queryMapLD.value = map

        shouldAppend = false

        if (type == 2) {
            model.fetchProductsByManufacturer(
                catId,
                map,
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
                        toast(errorMessage)
                    }
                })
        } else if (type == 1) {
            model.fetchProducts(catId, map, object : RequestCompleteListener<CategoryModel> {
                override fun onRequestSuccess(data: CategoryModel) {
                    isLoadingLD.value = false

                    productLiveData.value = data

                    preparePriceFilter(data.pagingFilteringContext)
                    prepareFilterAttribute(data.pagingFilteringContext)
                    setFilterVisibility(data.pagingFilteringContext)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.value = false
                    toast(errorMessage)
                }

            })
        } else if (type == 3) {
            model.fetchProductsByTag(catId, map, object : RequestCompleteListener<ProductByTagData> {
                override fun onRequestSuccess(data: ProductByTagData) {
                    isLoadingLD.value = false

                    tagLiveData.value = data

                    preparePriceFilter(data.pagingFilteringContext)
                    prepareFilterAttribute(data.pagingFilteringContext)
                    setFilterVisibility(data.pagingFilteringContext)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.value = false
                    toast(errorMessage)
                }

            })
        } else if (type == 4) {
            model.fetchProductsByVendor(catId, map, object : RequestCompleteListener<ProductByVendorData> {
                override fun onRequestSuccess(data: ProductByVendorData) {
                    isLoadingLD.value = false

                    vendorLiveData.value = data

                    preparePriceFilter(data.pagingFilteringContext)
                    prepareFilterAttribute(data.pagingFilteringContext)
                    setFilterVisibility(data.pagingFilteringContext)
                }

                override fun onRequestFailed(errorMessage: String) {
                    isLoadingLD.value = false
                    toast(errorMessage)
                }

            })
        }
    }

    private fun getQueryMap(type: Int, resetFilters: Boolean): MutableMap<String, String> {
        // calculate next page to load
        val currentPage = when (type) {
            1 -> productLiveData.value?.pagingFilteringContext?.pageNumber ?: 0
            2 -> manufacturerLD.value?.pagingFilteringContext?.pageNumber ?: 0
            3 -> tagLiveData.value?.pagingFilteringContext?.pageNumber ?: 0
            4 -> vendorLiveData.value?.pagingFilteringContext?.pageNumber ?: 0
            else -> 0
        }

        pageNumber = currentPage + 1

        // whether or not the new data should be added with existing data
        shouldAppend = currentPage>=1

        val map = queryMapLD.value ?: HashMap<String, String>()

        // reset filtering by clearing query params
        if(resetFilters) {
            map.clear()
        }

        // applying default query params
        map[Api.qs_page_number] = pageNumber.toString()
        map[Api.qs_page_size] = PrefSingleton.getPageSize().toString()

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