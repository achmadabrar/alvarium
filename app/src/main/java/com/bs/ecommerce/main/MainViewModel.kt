package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.ui.base.BaseViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.utils.toast


class MainViewModel  : BaseViewModel()
{

    var allCategoriesLD = MutableLiveData<List<String>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()

    fun getCityList(model: MainModel)
    {

       /* model.getCityList(object : RequestCompleteListener<MutableList<City>>
        {
            override fun onRequestSuccess(data: MutableList<City>) {
                cityListLiveData.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                cityListFailureLiveData.postValue(errorMessage)
            }
        })*/
    }


    fun getCategoryList(cityId: Int, model: MainModel)
    {

        isLoadingLD.postValue(true)

        model.getCategories(cityId, object : RequestCompleteListener<BaseResponse>
        {
            override fun onRequestSuccess(data: BaseResponse)
            {

              /*  val weatherData = WeatherData(
                    dateTime = data.dt.unixTimestampToDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                    weatherConditionIconDescription = data.weather[0].description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility/1000.0} KM",
                    sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                    sunset = data.sys.sunset.unixTimestampToTimeString()
                )*/

                //toast("Category API called")

                isLoadingLD.postValue(false)

                allCategoriesLD.postValue(listOf())
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                allCategoriesFailureLD.postValue(listOf())
            }
        })
    }

}


