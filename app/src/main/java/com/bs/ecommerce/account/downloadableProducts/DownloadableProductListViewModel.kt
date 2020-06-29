package com.bs.ecommerce.account.downloadableProducts

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductList
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.product.model.data.SampleDownloadResponse
import com.bs.ecommerce.product.model.data.UserAgreementData
import com.bs.ecommerce.product.model.data.UserAgreementResponse
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.OneTimeEvent
import com.bs.ecommerce.utils.Utils
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

class DownloadableProductListViewModel: BaseViewModel() {

    val productListLD = MutableLiveData<DownloadableProductList>()
    val userAgreementLD = MutableLiveData<OneTimeEvent<UserAgreementData>>()
    var productDownloadLD = MutableLiveData<OneTimeEvent<SampleDownloadResponse?>>()

    fun getProductList(model: DownloadableProductListModel) {

        isLoadingLD.value = true

        model.getProductList(object : RequestCompleteListener<DownloadableProductListResponse> {
            override fun onRequestSuccess(data: DownloadableProductListResponse) {
                isLoadingLD.value = false

                productListLD.value = data.downloadableProductList
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getUserAgreementData(guid: String, model: DownloadableProductListModel) {
        isLoadingLD.value = true

        model.userAgreement(guid, object : RequestCompleteListener<UserAgreementResponse> {

            override fun onRequestSuccess(data: UserAgreementResponse) {
                isLoadingLD.value = false

                data.data?.let {
                    userAgreementLD.value = OneTimeEvent(it)
                }
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }

    fun downloadProduct(guid: String, consent: String, model: DownloadableProductListModel) {

        model.downloadProduct(guid, consent, object: RequestCompleteListener<Response<ResponseBody>> {

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