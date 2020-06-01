package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.data.AppOptions
import com.bs.ecommerce.utils.PrefSingleton

class OptionViewModel : BaseViewModel() {

    var optionsLD = MutableLiveData<List<AppOptions>>()

    fun loadOptions(prefObject: PrefSingleton) {

        val data = mutableListOf<AppOptions>()

        data.add(AppOptions(R.string.title_scan_barcode, DbHelper.getString("nopstation.webapi.scanbarcode"), R.drawable.ic_settings))

        data.add(AppOptions(R.string.settings, DbHelper.getString("nopstation.webapi.settings"), R.drawable.ic_settings))

        data.add(AppOptions(R.string.privacy_policy, DbHelper.getString("nopstation.webapi.privacypolicy"), R.drawable.ic_privacy))

        data.add(AppOptions(R.string.title_about_us, DbHelper.getString("nopstation.webapi.aboutus"), R.drawable.ic_about_us))

        data.add(AppOptions(R.string.title_contact_us, DbHelper.getString("contactus"), R.drawable.ic_settings))

        // data.add(AppOptions(R.string.title_checkout, DbHelper.getString("Test"), R.drawable.ic_my_order))

        optionsLD.postValue(data)
    }
}