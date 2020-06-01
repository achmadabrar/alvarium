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

        data.add(AppOptions(R.string.title_about_us, R.drawable.ic_about_us))

        data.add(AppOptions(R.string.title_contact_us, R.drawable.ic_settings))


        optionsLD.postValue(data)
    }
}