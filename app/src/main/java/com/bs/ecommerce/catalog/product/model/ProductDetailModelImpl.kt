package com.bs.ecommerce.catalog.product.model

import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.catalog.common.AddToCartResponse
import com.bs.ecommerce.catalog.common.AddToWishListResponse
import com.bs.ecommerce.catalog.common.ProductDetailResponse
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
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
                                       callback: RequestCompleteListener<AddToCartResponse>
    )
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

        formValues.add(
            KeyValuePair().apply {
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
        callback: RequestCompleteListener<Response<ResponseBody>>
    ) {
        val x = RetroClient.api.sampleDownload(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<ResponseBody>>() {

                override fun onComplete() {
                    "downloadSample_".showLog("onComplete")
                }

                override fun onNext(t: Response<ResponseBody>) {
                    "downloadSample_".showLog("onNext")

                    t.body()?.let {
                        callback.onRequestSuccess(t)
                    } ?: run {
                        callback.onRequestFailed(TextUtils.getErrorMessage(t))
                    }
                }

                override fun onError(e: Throwable) {
                    "downloadSample_".showLog("onError")
                    callback.onRequestFailed(e.message ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
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