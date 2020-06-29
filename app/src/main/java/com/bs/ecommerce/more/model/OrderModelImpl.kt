package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.product.model.data.OrderDetailsResponse
import com.bs.ecommerce.product.model.data.OrderHistoryData
import com.bs.ecommerce.product.model.data.OrderHistoryResponse
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

class OrderModelImpl : OrderModel {

    override fun getOrderHistory(callback: RequestCompleteListener<OrderHistoryData>) {
        RetroClient.api.getOrderHistory().enqueue(object :
            Callback<OrderHistoryResponse> {
            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<OrderHistoryResponse>, response: Response<OrderHistoryResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.orderHistory as OrderHistoryData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun getOrderDetails(
        orderId: Int,
        callback: RequestCompleteListener<OrderDetailsData>
    ) {

        RetroClient.api.getOrderDetails(orderId).enqueue(object :
            Callback<OrderDetailsResponse> {

            override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<OrderDetailsResponse>,
                response: Response<OrderDetailsResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()?.data as OrderDetailsData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun reorder(orderId: Int, callback: RequestCompleteListener<Boolean>) {

        RetroClient.api.reorder(orderId).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(true)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun downloadPdfInvoice(orderId: Int, callback: RequestCompleteListener<Response<ResponseBody>>) {

        val observabled = RetroClient.api.downloadPdfInvoice(orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<ResponseBody>>() {

                override fun onComplete() {
                    "invoice_".showLog("onComplete")
                }

                override fun onNext(t: Response<ResponseBody>) {
                    "invoice_".showLog("onNext")

                    t.body()?.let {
                        callback.onRequestSuccess(t)
                    } ?: run {
                        callback.onRequestFailed(TextUtils.getErrorMessage(t))
                    }
                }

                override fun onError(e: Throwable) {
                    "invoice_".showLog("onError")
                    callback.onRequestFailed(e.message ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
                }

            })
    }

    override fun downloadOrderNotes(notesId: Int, callback: RequestCompleteListener<Response<ResponseBody>>) {

        val observabled = RetroClient.api.orderNoteDownload(notesId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<ResponseBody>>() {

                override fun onComplete() {
                    "noteDownload_".showLog("onComplete")
                }

                override fun onNext(t: Response<ResponseBody>) {
                    "noteDownload_".showLog("onNext")

                    t.body()?.let {
                        callback.onRequestSuccess(t)
                    } ?: run {
                        callback.onRequestFailed(TextUtils.getErrorMessage(t))
                    }
                }

                override fun onError(e: Throwable) {
                    "noteDownload_".showLog("onError")
                    callback.onRequestFailed(e.message ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
                }

            })
    }
}