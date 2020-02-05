package com.bs.ecommerce.main.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.BaseResponse
import com.hellohasan.weatherappmvvm.features.weather_info_show.model.data_class.City

interface MainModel
{
    fun getCityList(callback: RequestCompleteListener<MutableList<City>>)
    fun getCategories(cityId: Int, callback: RequestCompleteListener<BaseResponse>)
}