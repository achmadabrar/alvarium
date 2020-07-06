package com.bs.ecommerce.account.downloadableProducts.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.account.downloadableProducts.model.data.UserAgreementResponse
import com.bs.ecommerce.account.returnRequests.model.data.ReturnRequestHistoryData
import okhttp3.ResponseBody
import retrofit2.Response

interface DownloadableProductListModel {

    fun getProductList(
        callback: RequestCompleteListener<DownloadableProductListResponse>
    )

    fun userAgreement(
        guid: String,
        callback: RequestCompleteListener<UserAgreementResponse>
    )

    fun downloadProduct(
        guid: String,
        consent: String,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )
}