package com.bs.ecommerce.product.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.AddToWishListResponse
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.data.SampleDownloadResponse
import com.bs.ecommerce.utils.TextUtils
import com.google.gson.GsonBuilder
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

                if (response.body() != null && response.code()==200)
                    callback.onRequestSuccess(response.body() as ProductDetailResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun addProductToCartModel(productId: Long,
                                       cartTypeId: Long,
                                       KeyValueFormData: KeyValueFormData,
                                       callback: RequestCompleteListener<AddToCartResponse>)
    {

        RetroClient.api.addProductIntoCartAPI(productId, cartTypeId, KeyValueFormData).enqueue(object : Callback<AddToCartResponse>
        {
            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<AddToCartResponse>, response: Response<AddToCartResponse>) {

                if (response.body() != null && response.code()==200)
                    callback.onRequestSuccess(response.body() as AddToCartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun addProductToWishList(
        productId: Long,
        cartType: Long,
        callback: RequestCompleteListener<AddToWishListResponse>
    ) {
        // create request body
        val formValues: MutableList<KeyValuePair> = mutableListOf()

        formValues.add(KeyValuePair().apply {
            key = "addtocart_$productId.EnteredQuantity"
            value = "1"
        })

        RetroClient.api.addToCartFromList(productId, cartType, KeyValueFormData(formValues))
            .enqueue(object : Callback<AddToWishListResponse> {

                override fun onFailure(call: Call<AddToWishListResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")
                }

                override fun onResponse(
                    call: Call<AddToWishListResponse>,
                    response: Response<AddToWishListResponse>
                ) {
                    if (response.body() != null)
                        callback.onRequestSuccess(response.body() as AddToWishListResponse)

                    else if (response.code() == 300 || response.code() == 400)
                    {
                        val errorBody = GsonBuilder().create().fromJson(response.errorBody()!!.string(), AddToWishListResponse::class.java)
                        callback.onRequestSuccess(errorBody as AddToWishListResponse)
                    }
                    else
                        callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }

            })
    }

    override fun downloadSample(
        productId: Long,
        callback: RequestCompleteListener<SampleDownloadResponse>
    ) {
        RetroClient.api.sampleDownload(productId)
            .enqueue(object : Callback<SampleDownloadResponse> {
                override fun onResponse(
                    call: Call<SampleDownloadResponse>,
                    response: Response<SampleDownloadResponse>
                ) {
                    if (response.body() != null && response.code() == 200)
                        callback.onRequestSuccess(response.body() as SampleDownloadResponse)
                    else
                        callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }


                override fun onFailure(call: Call<SampleDownloadResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")
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
                        callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }


                override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")
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
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")
                }
            })
    }

}