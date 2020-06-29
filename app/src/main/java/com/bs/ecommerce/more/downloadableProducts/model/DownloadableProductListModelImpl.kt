package com.bs.ecommerce.more.downloadableProducts.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.UserAgreementResponse
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DownloadableProductListModelImpl : DownloadableProductListModel {

    override fun getProductList(callback: RequestCompleteListener<DownloadableProductListResponse>) {

        RetroClient.api.getDownloadableProducts().enqueue(object : Callback<DownloadableProductListResponse> {

            override fun onFailure(call: Call<DownloadableProductListResponse>, t: Throwable)

                    = callback.onRequestFailed(t.localizedMessage ?: "Unknown")


            override fun onResponse(call: Call<DownloadableProductListResponse>, response: Response<DownloadableProductListResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as DownloadableProductListResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun userAgreement(
        guid: String,
        callback: RequestCompleteListener<UserAgreementResponse>
    ) {
        RetroClient.api.userAgreement(guid)
            .enqueue(object : Callback<UserAgreementResponse> {

                override fun onFailure(call: Call<UserAgreementResponse>, t: Throwable) =
                    callback.onRequestFailed(t.localizedMessage ?: "Unknown")


                override fun onResponse(
                    call: Call<UserAgreementResponse>,
                    response: Response<UserAgreementResponse>
                ) {
                    if (response.body() != null && response.code() == 200)
                        callback.onRequestSuccess(response.body() as UserAgreementResponse)
                    else
                        callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            })
    }

    override fun downloadProduct(
        guid: String,
        consent: String,
        callback: RequestCompleteListener<Response<ResponseBody>>
    ) {
        val observabled = RetroClient.api.downloadProduct(guid, consent)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<ResponseBody>>() {

                override fun onComplete() {
                    "downloadProduct_".showLog("onComplete")
                }

                override fun onNext(t: Response<ResponseBody>) {
                    "downloadProduct_".showLog("onNext")

                    t.body()?.let {
                        callback.onRequestSuccess(t)
                    } ?: run {
                        callback.onRequestFailed(TextUtils.getErrorMessage(t))
                    }
                }

                override fun onError(e: Throwable) {
                    "downloadProduct_".showLog("onError")
                    callback.onRequestFailed(e.message ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
                }

            })
    }
}