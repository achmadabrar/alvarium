package com.bs.ecommerce.checkout.model

import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckoutModelImpl : CheckoutModel
{

    override fun getBillingForm(callback: RequestCompleteListener<BillingAddressResponse>)
    {

        RetroClient.api.getBillingFormAPI().enqueue(object : Callback<BillingAddressResponse>
        {
            override fun onResponse(call: Call<BillingAddressResponse>, response: Response<BillingAddressResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as BillingAddressResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<BillingAddressResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun getStatesByCountryForm(countryCode: String, callback: RequestCompleteListener<StateListResponse>)
    {

        RetroClient.api.getStatesAPI(countryCode).enqueue(object : Callback<StateListResponse>
        {
            override fun onResponse(call: Call<StateListResponse>, response: Response<StateListResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as StateListResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }


    override fun saveNewBilling(billingAddress: SaveBillingPostData, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {
        RetroClient.api.saveNewBillingAPI(billingAddress).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun saveExistingBilling(postModel: ExistingAddress, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {
        RetroClient.api.saveExistingBillingAPI(postModel).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun saveNewShipping(shippingAddress: SaveShippingPostData, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {
        RetroClient.api.saveNewShippingAPI(shippingAddress).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun saveExistingShipping(postModel: ExistingAddress, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {
        RetroClient.api.saveExistingShippingAPI(postModel).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun saveShippingMethod(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {

        RetroClient.api.saveShippingMethodAPI(KeyValueFormData).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    override fun savePaymentMethod(KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CheckoutSaveResponse>)
    {

        RetroClient.api.savePaymentMethodAPI(KeyValueFormData).enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }


    override fun getCheckoutConfirmInformation(callback: RequestCompleteListener<ConfirmOrderResponse>)
    {

        RetroClient.api.getCheckoutConfirmInformationAPI().enqueue(object : Callback<ConfirmOrderResponse>
        {
            override fun onResponse(call: Call<ConfirmOrderResponse>, response: Response<ConfirmOrderResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as ConfirmOrderResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<ConfirmOrderResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }
    override fun submitConfirmOrder(callback: RequestCompleteListener<CheckoutSaveResponse>)
    {

        RetroClient.api.submitConfirmOrderAPI().enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }
    override fun completeOrder(callback: RequestCompleteListener<CheckoutSaveResponse>)
    {

        RetroClient.api.completeOrderAPI().enqueue(object : Callback<CheckoutSaveResponse>
        {
            override fun onResponse(call: Call<CheckoutSaveResponse>, response: Response<CheckoutSaveResponse>)
            {
                if(response.body() != null && response.code() == 200)
                    callback.onRequestSuccess(response.body() as CheckoutSaveResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

            override fun onFailure(call: Call<CheckoutSaveResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "")
            }
        })
    }

    /*private fun genericCallback(callback: RequestCompleteListener<BaseResponse>, response: Response<BaseResponse>)
    {
        if (response.body() != null)
            callback.onRequestSuccess(response.body() as BaseResponse)

        else if (response.code() == 300 || response.code() == 400 || response.code() == 500)
        {
            val gson = GsonBuilder().create()
            val errorBody = gson.fromJson(response.errorBody()?.string() ?: "", BaseResponse::class.java)

            callback.onRequestSuccess(errorBody as BaseResponse)
        }
        else
            callback.onRequestFailed(response.message())
    }*/
}