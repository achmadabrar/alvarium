package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.model.data.AppOptions
import com.bs.ecommerce.utils.PrefSingleton

class OptionViewModel : BaseViewModel() {

    var optionsLD = MutableLiveData<List<AppOptions>>()

    fun loadOptions(prefObject: PrefSingleton) {

        val data = mutableListOf<AppOptions>()

        data.add(AppOptions(R.string.title_scan_barcode, R.drawable.ic_settings))

        data.add(AppOptions(R.string.settings, R.drawable.ic_settings))

        data.add(AppOptions(R.string.privacy_policy, R.drawable.ic_privacy))

        data.add(AppOptions(R.string.my_orders, R.drawable.ic_my_order))

        if(prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
            data.add(AppOptions(R.string.log_out, R.drawable.ic_login))
        else
            data.add(AppOptions(R.string.login, R.drawable.ic_login))

        data.add(AppOptions(R.string.title_checkout, R.drawable.ic_my_order))

        data.add(AppOptions(R.string.title_wishlist, R.drawable.ic_heart))

        optionsLD.postValue(data)
    }

    /*fun changeLogInText()
    {
        //val data = optionsLD.value as MutableList<AppOptions>

        (optionsLD.value as MutableList<AppOptions>).remove(AppOptions(R.string.login, R.drawable.ic_login))

        (optionsLD.value as MutableList<AppOptions>).add(AppOptions(R.string.log_out, R.drawable.ic_login))

    }*/
}