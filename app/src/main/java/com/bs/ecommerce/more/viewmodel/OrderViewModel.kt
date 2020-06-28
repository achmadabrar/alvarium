package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.OrderModel
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.product.model.data.OrderHistoryData
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.OneTimeEvent
import com.bs.ecommerce.utils.Utils
import okhttp3.ResponseBody

class OrderViewModel : BaseViewModel() {

    var orderHistoryLD = MutableLiveData<OrderHistoryData>()
    var orderDetailsLD = MutableLiveData<OrderDetailsData>()
    var reorderLD = MutableLiveData<OneTimeEvent<Boolean>>()
    //var invoiceDownloadLD = MutableLiveData<OneTimeEvent<ResponseBody>>()

    fun getOrderHistory(model: OrderModel) {
        isLoadingLD.value = true

        model.getOrderHistory(object : RequestCompleteListener<OrderHistoryData> {
            override fun onRequestSuccess(data: OrderHistoryData) {
                isLoadingLD.value = false

                orderHistoryLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }

        })
    }

    fun getOrderDetails(orderId: Int, model: OrderModel) {
        isLoadingLD.value = true

        model.getOrderDetails(orderId, object : RequestCompleteListener<OrderDetailsData> {
            override fun onRequestSuccess(data: OrderDetailsData) {
                isLoadingLD.value = false
                orderDetailsLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false

            }

        })
    }

    fun reorder(orderId: Int, model: OrderModel) {
        isLoadingLD.value = true

        model.reorder(orderId, object : RequestCompleteListener<Boolean> {
            override fun onRequestSuccess(data: Boolean) {
                isLoadingLD.value = false
                reorderLD.value = OneTimeEvent(true)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false

            }

        })
    }

    fun downloadPdfInvoice(orderId: Int, model: OrderModel) {
        isLoadingLD.value = true

        model.downloadPdfInvoice(orderId, object : RequestCompleteListener<ResponseBody> {

            override fun onRequestSuccess(data: ResponseBody) {

                MyApplication.mAppContext?.let {
                    val done = Utils().writeResponseBodyToDisk(it, "order_$orderId", data)

                    if(done)
                        toast("File order_$orderId downloaded to Download directory")
                    else
                        toast(DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
                }

                isLoadingLD.value = false
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }
}