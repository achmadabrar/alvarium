package com.bs.ecommerce.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



open class BaseViewModel : ViewModel()
{
    var isLoadingLD = MutableLiveData<Boolean>()

    override fun onCleared()
    {
        super.onCleared()
    }


}