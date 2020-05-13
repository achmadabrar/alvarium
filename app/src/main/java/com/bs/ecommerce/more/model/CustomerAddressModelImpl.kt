package com.bs.ecommerce.more.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.*
import com.bs.ecommerce.utils.TextUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerAddressModelImpl : CustomerAddressModel {

    override fun getCustomerAddresses(callback: RequestCompleteListener<CustomerAddressData>) {

        RetroClient.api.getCustomerAddresses().enqueue(object :
            Callback<CustomerAddressResponse> {

            override fun onFailure(call: Call<CustomerAddressResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<CustomerAddressResponse>,
                response: Response<CustomerAddressResponse>
            ) {
                if (response.body() != null && response.body()?.data != null)
                    callback.onRequestSuccess(response.body()?.data as CustomerAddressData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun getFormDataForNewAddress(callback: RequestCompleteListener<EditCustomerAddressData>) {

        RetroClient.api.getAddAddressData().enqueue(object :
            Callback<EditCustomerAddressResponse> {

            override fun onFailure(call: Call<EditCustomerAddressResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<EditCustomerAddressResponse>,
                response: Response<EditCustomerAddressResponse>
            ) {
                if (response.body() != null && response.body()?.data != null)
                    callback.onRequestSuccess(response.body()?.data as EditCustomerAddressData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun getFormDataForEditAddress(
        addressId: Int,
        callback: RequestCompleteListener<EditCustomerAddressData>
    ) {

        RetroClient.api.getEditAddressData(addressId).enqueue(object :
            Callback<EditCustomerAddressResponse> {

            override fun onFailure(call: Call<EditCustomerAddressResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<EditCustomerAddressResponse>,
                response: Response<EditCustomerAddressResponse>
            ) {
                if (response.body() != null && response.body()?.data != null)
                    callback.onRequestSuccess(response.body()?.data as EditCustomerAddressData)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }

    override fun updateAddress(
        address: AddressModel,
        customAttributes: List<KeyValuePair>,
        callback: RequestCompleteListener<Any?>
    ) {
        val reqBody =
            EditCustomerAddressResponse(EditCustomerAddressData(address, CustomProperties()))
        reqBody.formValues = customAttributes

        RetroClient.api.editAddress(address.id!!, reqBody).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200)
                    callback.onRequestSuccess(null)
                else
                    callback.onRequestFailed(
                        TextUtils.getErrorMessage(response)
                    )
            }

        })
    }

    override fun saveAddress(
        address: AddressModel,
        customAttributes: List<KeyValuePair>,
        callback: RequestCompleteListener<Any?>
    ) {
        val reqBody = EditCustomerAddressResponse(EditCustomerAddressData(address, CustomProperties()))
        reqBody.formValues = customAttributes

        RetroClient.api.saveCustomerAddress(reqBody).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 201)
                    callback.onRequestSuccess(null)
                else
                    callback.onRequestFailed(
                        TextUtils.getErrorMessage(response)
                    )
            }

        })
    }

    override fun deleteAddress(addressId: Int, callback: RequestCompleteListener<Any?>) {

        RetroClient.api.deleteCustomerAddress(addressId).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.code() == 200)
                    callback.onRequestSuccess(null)
                else
                    callback.onRequestFailed(response.message())
            }

        })
    }
}