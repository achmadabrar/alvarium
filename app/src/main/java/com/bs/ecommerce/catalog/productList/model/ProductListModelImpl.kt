package com.bs.ecommerce.catalog.productList.model

import com.bs.ecommerce.catalog.common.*
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.home.homepage.model.data.Manufacturer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListModelImpl :
    ProductListModel {

    override fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<CategoryModel>
    ) {

        RetroClient.api.getProductList(catId, queryMap).enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as CategoryModel)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun fetchProductsByTag(
        tagId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductByTagData>
    ) {

        RetroClient.api.getProductByTag(tagId, queryMap).enqueue(object : Callback<ProductByTagResponse> {
            override fun onFailure(call: Call<ProductByTagResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<ProductByTagResponse>,
                response: Response<ProductByTagResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as ProductByTagData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun fetchProductsByVendor(
        vendorId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<ProductByVendorData>
    ) {

        RetroClient.api.getProductByVendor(vendorId, queryMap).enqueue(object : Callback<ProductByVendorResponse> {
            override fun onFailure(call: Call<ProductByVendorResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<ProductByVendorResponse>,
                response: Response<ProductByVendorResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as ProductByVendorData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun fetchProductsByManufacturer(
        manufacturerId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<Manufacturer>
    ) {
        RetroClient.api.getProductListByManufacturer(manufacturerId, queryMap).enqueue(object : Callback<ProductByManufacturerResponse> {
            override fun onFailure(call: Call<ProductByManufacturerResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<ProductByManufacturerResponse>,
                response: Response<ProductByManufacturerResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.manufacturer as Manufacturer)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun applyFilter(url: String, callback: RequestCompleteListener<CategoryModel>) {
        RetroClient.api.applyFilter(url).enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as CategoryModel)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }


}