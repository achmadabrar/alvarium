package com.bs.ecommerce.home.homepage.model

import android.util.Log
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.StringResourceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StringResourceModelImpl:  StringResourceModel{

    override fun getStringResource(
        languageId: Int,
        callback: RequestCompleteListener<StringResourceResponse>
    ) {

        RetroClient.api.getStringResource(languageId).enqueue(object : Callback<StringResourceResponse> {

            override fun onFailure(call: Call<StringResourceResponse>, t: Throwable) {
                Log.e("nop_", "got getStringResource failed", t)

                callback.onRequestFailed(t.message ?: "Unknown")
            }

            override fun onResponse(
                call: Call<StringResourceResponse>,
                response: Response<StringResourceResponse>
            ) {
                Log.d("nop_", "got getStringResource. ${response.body()?.stringResource?.size}")

                callback.onRequestSuccess(response.body() as StringResourceResponse)
            }

        })
    }
}