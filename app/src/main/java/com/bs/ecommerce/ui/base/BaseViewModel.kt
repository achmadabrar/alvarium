package com.bs.ecommerce.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



open class BaseViewModel : ViewModel()
{
    val isLoadingLD = MutableLiveData<Boolean>()

    override fun onCleared()
    {
        super.onCleared()
    }


}