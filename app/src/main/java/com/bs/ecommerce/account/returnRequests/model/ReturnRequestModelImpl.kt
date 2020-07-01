package com.bs.ecommerce.account.returnRequests.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.account.downloadableProducts.model.data.UserAgreementResponse
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryData
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryResponse
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

class ReturnRequestModelImpl :
    ReturnRequestModel {

    override fun getReturnRequestHistory(callback: RequestCompleteListener<ReturnRequestHistoryData>) {

        RetroClient.api.getReturnRequestHistory().enqueue(object : Callback<ReturnRequestHistoryResponse> {

            override fun onFailure(call: Call<ReturnRequestHistoryResponse>, t: Throwable)

                    = callback.onRequestFailed(t.localizedMessage ?: "Unknown")


            override fun onResponse(call: Call<ReturnRequestHistoryResponse>, response: Response<ReturnRequestHistoryResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body()!!.ReturnRequestHistoryData as ReturnRequestHistoryData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }



    override fun downloadFile(
        guid: String,
        callback: RequestCompleteListener<Response<ResponseBody>>
    ) {
        val observabled = RetroClient.api.getReturnRequestDownloadFile(guid)
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