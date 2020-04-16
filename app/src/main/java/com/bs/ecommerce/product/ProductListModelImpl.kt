package com.bs.ecommerce.product

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.data.CategoryResponse
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import com.bs.ecommerce.product.data.ProductByManufacturerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListModelImpl : ProductListModel {

    override fun fetchProducts(
        catId: Long,
        queryMap: Map<String, String>,
        callback: RequestCompleteListener<CategoryModel>) {

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


}