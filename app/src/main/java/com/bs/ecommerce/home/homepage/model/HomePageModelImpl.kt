package com.bs.ecommerce.home.homepage.model

import android.content.Context
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.home.homepage.model.data.SliderResponse
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.HomePageCategoryResponse
import com.bs.ecommerce.product.data.Manufacturer
import com.bs.ecommerce.product.data.ManufacturerResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageModelImpl(private val context: Context) : HomePageModel {

    override fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>) {

        RetroClient.api.getHomeFeaturedProducts()
            .enqueue(object : Callback<HomePageProductResponse> {
                override fun onResponse(
                    call: Call<HomePageProductResponse>,
                    response: Response<HomePageProductResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body()!!)
                    else
                        callback.onRequestFailed(response.message())
                }


                override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage!!)
                }
            })


    }

    override fun fetchBestSellingProducts(callback: RequestCompleteListener<HomePageProductResponse>) {
        RetroClient.api.getHomeBestSellerProducts()
            .enqueue(object : Callback<HomePageProductResponse> {
                override fun onResponse(
                    call: Call<HomePageProductResponse>,
                    response: Response<HomePageProductResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body()!!)
                    else
                        callback.onRequestFailed(response.message())
                }


                override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
                }
            })
    }

    override fun fetchHomePageCategoryList(callback: RequestCompleteListener<List<CategoryModel>>) {
        RetroClient.api.getHomePageCategoriesWithProducts()
            .enqueue(object : Callback<HomePageCategoryResponse> {
                override fun onFailure(call: Call<HomePageCategoryResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
                }

                override fun onResponse(call: Call<HomePageCategoryResponse>, response: Response<HomePageCategoryResponse>) {
                    callback.onRequestSuccess(response.body()?.categoryList ?: listOf())
                }

            })
    }

    override fun fetchManufacturers(callback: RequestCompleteListener<List<Manufacturer>>) {

        RetroClient.api.getHomeManufacturer().enqueue(object : Callback<ManufacturerResponse> {
            override fun onFailure(call: Call<ManufacturerResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(call: Call<ManufacturerResponse>, response: Response<ManufacturerResponse>) {
                callback.onRequestSuccess(response.body()?.manufacturerList ?: listOf())
            }

        })
    }

    override fun fetchBannerImages(callback: RequestCompleteListener<SliderData>) {

        RetroClient.api.getHomeSlider().enqueue(object : Callback<SliderResponse> {
            override fun onFailure(call: Call<SliderResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(call: Call<SliderResponse>, response: Response<SliderResponse>) {
                callback.onRequestSuccess(response.body()?.data ?: SliderData())
            }

        })
    }


}