package com.bs.ecommerce.account.returnRequests

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductList
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.account.returnRequests.model.ReturnRequestModel
import com.bs.ecommerce.account.returnRequests.model.ReturnRequestModelImpl
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryData
import com.bs.ecommerce.catalog.common.SampleDownloadResponse
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.OneTimeEvent
import com.bs.ecommerce.utils.Utils
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

class ReturnRequestHistoryViewModel: BaseViewModel() {

    val returnRequestHistoryLD = MutableLiveData<ReturnRequestHistoryData>()
    var productDownloadLD = MutableLiveData<OneTimeEvent<SampleDownloadResponse?>>()

    fun getReturnRequestHistory(model: ReturnRequestModel) {

        isLoadingLD.value = true

        model.getReturnRequestHistory(object :
            RequestCompleteListener<ReturnRequestHistoryData> {
            override fun onRequestSuccess(data: ReturnRequestHistoryData) {
                isLoadingLD.value = false

                returnRequestHistoryLD.value = data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun downloadFile(guid: String, model: ReturnRequestModel) {

        model.downloadFile(guid, object:
            RequestCompleteListener<Response<ResponseBody>> {

            override fun onRequestSuccess(data: Response<ResponseBody>) {

                try {
                    val contentType = data.body()?.contentType()

                    if(contentType?.subtype?.equals("json") == true) {
                        val response = Gson().fromJson(data.body()?.string(), SampleDownloadResponse::class.java)

                        if(response.errorsAsFormattedString.isNotEmpty()) {
                            toast(response.errorsAsFormattedString)
                            productDownloadLD.value = OneTimeEvent(null)
                        } else {
                            productDownloadLD.value = OneTimeEvent(response)
                        }

                    } else {
                        val done = Utils().writeResponseBodyToDisk(guid, data)

                        if(done)
                            toast(DbHelper.getString(Const.FILE_DOWNLOADED))
                        else
                            toast(DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))

                        productDownloadLD.value = OneTimeEvent(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                productDownloadLD.value = OneTimeEvent(null)
            }

            override fun onRequestFailed(errorMessage: String) {
                productDownloadLD.value = OneTimeEvent(null)
                toast(errorMessage)
            }

        })
    }


}