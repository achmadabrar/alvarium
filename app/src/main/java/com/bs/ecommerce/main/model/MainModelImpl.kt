package com.bs.ecommerce.main.model

import android.content.Context
import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.networking.RetroClient
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.bs.ecommerce.common.RequestCompleteListener
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainModelImpl(private val context: Context): MainModel
{

    override fun getCityList(callback: RequestCompleteListener<MutableList<City>>)
    {
        try {
            val stream = context.assets.open("city_list.json")

            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val tContents  = String(buffer)

            val groupListType = object : TypeToken<ArrayList<City>>() {}.type
            val gson = GsonBuilder().create()
            val cityList: MutableList<City> = gson.fromJson(tContents, groupListType)

            callback.onRequestSuccess(cityList)

        } catch (e: IOException) {
            e.printStackTrace()
            callback.onRequestFailed(e.localizedMessage!!)
        }
    }

    override fun getCategories(cityId: Int, callback: RequestCompleteListener<BaseResponse>)
    {

        RetroClient.api.getCategoryList().enqueue(object : Callback<BaseResponse>
        {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }


            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }
}