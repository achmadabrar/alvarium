package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.ContactUsData
import com.bs.ecommerce.product.model.data.ContactUsResponse
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonModelImpl: CommonModel {

    override fun postCustomerEnquiry(
        userData: ContactUsData,
        callback: RequestCompleteListener<ContactUsResponse>
    ) {
        val reqBody = ContactUsResponse(userData)

        RetroClient.api.contactUs(reqBody).enqueue(object : Callback<ContactUsResponse> {

            override fun onFailure(call: Call<ContactUsResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<ContactUsResponse>, response: Response<ContactUsResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ContactUsResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }
}