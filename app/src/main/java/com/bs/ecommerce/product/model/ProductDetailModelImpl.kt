package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.data.UpdateCartPostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailModelImpl :
    ProductDetailModel {

    override fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>) {

        RetroClient.api.getProductDetails(productId).enqueue(object : Callback<ProductDetailResponse> {
            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ProductDetailResponse>, response: Response<ProductDetailResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as ProductDetailResponse)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun addProductToCartModel(productId: Long,
                                       cartTypeId: Long,
                                       updateCartPostData: UpdateCartPostData,
                                       callback: RequestCompleteListener<AddToCartResponse>)
    {

        RetroClient.api.addProductIntoCartAPI(productId, cartTypeId, updateCartPostData).enqueue(object : Callback<AddToCartResponse>
        {
            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<AddToCartResponse>, response: Response<AddToCartResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as AddToCartResponse)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun addProductToWishList(
        productId: Long,
        callback: RequestCompleteListener<AddToCartResponse>
    ) {

        RetroClient.api.addProductIntoCartAPI(productId, Api.typeWishList, UpdateCartPostData())
            .enqueue(object : Callback<AddToCartResponse> {
                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")
                }

                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body() as AddToCartResponse)
                    else
                        callback.onRequestFailed(response.message())
                }

            })
    }

    override fun getRelatedProducts(
        productId: Long,
        thumbnailSizePx: Int,
        callback: RequestCompleteListener<HomePageProductResponse>
    ) {
        RetroClient.api.getRelatedProducts(productId, thumbnailSizePx)
            .enqueue(object : Callback<HomePageProductResponse> {
                override fun onResponse(
                    call: Call<HomePageProductResponse>,
                    response: Response<HomePageProductResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body() as HomePageProductResponse)
                    else
                        callback.onRequestFailed(response.message())
                }


                override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "")
                }
            })
    }

    override fun getAlsoPurchasedProducts(
        productId: Long,
        thumbnailSizePx: Int,
        callback: RequestCompleteListener<HomePageProductResponse>
    ) {
        RetroClient.api.getSimilarProducts(productId, thumbnailSizePx)
            .enqueue(object : Callback<HomePageProductResponse> {
                override fun onResponse(
                    call: Call<HomePageProductResponse>,
                    response: Response<HomePageProductResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body() as HomePageProductResponse)
                    else
                        callback.onRequestFailed(response.message())
                }


                override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "")
                }
            })
    }

}