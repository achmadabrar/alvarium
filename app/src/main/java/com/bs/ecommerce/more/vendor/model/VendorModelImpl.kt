package com.bs.ecommerce.more.vendor.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.more.vendor.model.data.GetAllVendorsResponse
import com.bs.ecommerce.more.vendor.model.data.GetContactVendorResponse
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorModelImpl: VendorModel {

    override fun getAllVendors(callback: RequestCompleteListener<GetAllVendorsResponse>) {

        RetroClient.api.getAllVendors().enqueue(object :
            Callback<GetAllVendorsResponse> {

            override fun onFailure(call: Call<GetAllVendorsResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetAllVendorsResponse>,
                response: Response<GetAllVendorsResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetAllVendorsResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun getContactVendorModel(
        vendorId: Int,
        callback: RequestCompleteListener<GetContactVendorResponse>
    ) {

        RetroClient.api.getContactVendorModel(vendorId).enqueue(object :
            Callback<GetContactVendorResponse> {

            override fun onFailure(call: Call<GetContactVendorResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetContactVendorResponse>,
                response: Response<GetContactVendorResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as GetContactVendorResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }

    override fun postContactVendorModel(
        reqBody: GetContactVendorResponse,
        callback: RequestCompleteListener<GetContactVendorResponse>
    ) {
        RetroClient.api.postContactVendorModel(reqBody).enqueue(object :
            Callback<GetContactVendorResponse> {

            override fun onFailure(call: Call<GetContactVendorResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Something went wrong")
            }

            override fun onResponse(
                call: Call<GetContactVendorResponse>,
                response: Response<GetContactVendorResponse>
            ) {
                if (response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as GetContactVendorResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }
}