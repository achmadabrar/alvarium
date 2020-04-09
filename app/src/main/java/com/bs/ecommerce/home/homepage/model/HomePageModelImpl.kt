package com.bs.ecommerce.home.homepage.model

import android.content.Context
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageModelImpl(private val context: Context): HomePageModel
{

    override fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>)
    {

        RetroClient.api.getHomeFeaturedProducts().enqueue(object : Callback<HomePageProductResponse>
        {
            override fun onResponse(call: Call<HomePageProductResponse>, response: Response<HomePageProductResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<HomePageProductResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }




}