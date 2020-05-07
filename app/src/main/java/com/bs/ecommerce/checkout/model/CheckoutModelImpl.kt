package com.bs.ecommerce.checkout.model

import android.content.Context
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckoutModelImpl(private val context: Context): CheckoutModel
{

    override fun getBillingForm(callback: RequestCompleteListener<BillingAddressResponse>)
    {

        RetroClient.api.getBillingFormAPI().enqueue(object : Callback<BillingAddressResponse>
        {
            override fun onResponse(call: Call<BillingAddressResponse>, response: Response<BillingAddressResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<BillingAddressResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getStatesByCountryForm(countryCode: String, callback: RequestCompleteListener<StateListResponse>)
    {

        RetroClient.api.getStatesAPI(countryCode).enqueue(object : Callback<StateListResponse>
        {
            override fun onResponse(call: Call<StateListResponse>, response: Response<StateListResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }


    override fun saveNewBilling(billingAddress: SaveBillingPostData, callback: RequestCompleteListener<SaveBillingResponse>)
    {
        RetroClient.api.saveNewBillingAPI(billingAddress).enqueue(object : Callback<SaveBillingResponse>
        {
            override fun onResponse(call: Call<SaveBillingResponse>, response: Response<SaveBillingResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), SaveBillingResponse::class.java)

                    callback.onRequestSuccess(errorBody as SaveBillingResponse)
                }
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<SaveBillingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun saveExistingBilling(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<SaveBillingResponse>)
    {
        RetroClient.api.saveExistingBillingAPI(keyValueFormData).enqueue(object : Callback<SaveBillingResponse>
        {
            override fun onResponse(call: Call<SaveBillingResponse>, response: Response<SaveBillingResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), SaveBillingResponse::class.java)

                    callback.onRequestSuccess(errorBody as SaveBillingResponse)
                }
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<SaveBillingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun saveNewShipping(shippingAddress: SaveShippingPostData, callback: RequestCompleteListener<SaveBillingResponse>)
    {
        RetroClient.api.saveNewShippingAPI(shippingAddress).enqueue(object : Callback<SaveBillingResponse>
        {
            override fun onResponse(call: Call<SaveBillingResponse>, response: Response<SaveBillingResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), SaveBillingResponse::class.java)

                    callback.onRequestSuccess(errorBody as SaveBillingResponse)
                }
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<SaveBillingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun saveExistingShipping(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<SaveBillingResponse>)
    {
        RetroClient.api.saveExistingShippingAPI(keyValueFormData).enqueue(object : Callback<SaveBillingResponse>
        {
            override fun onResponse(call: Call<SaveBillingResponse>, response: Response<SaveBillingResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)

                else if (response.code() == 300 || response.code() == 400)
                {
                    val gson = GsonBuilder().create()
                    val errorBody = gson.fromJson(response.errorBody()!!.string(), SaveBillingResponse::class.java)

                    callback.onRequestSuccess(errorBody as SaveBillingResponse)
                }
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<SaveBillingResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getShippingMethod(callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.getShippingMethodAPI().enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getPaymentMethod(callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.getPaymentMethodAPI().enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getCheckoutConfirmInformation(callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.getCheckoutConfirmInformationAPI().enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }


    override fun saveShippingMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.saveShippingMethodAPI(checkoutPostData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun savePaymentMethod(checkoutPostData: CheckoutPostData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.savePaymentMethodAPI(checkoutPostData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

}