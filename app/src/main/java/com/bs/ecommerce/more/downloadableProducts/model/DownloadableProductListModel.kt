package com.bs.ecommerce.more.downloadableProducts.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.product.model.data.UserAgreementResponse
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