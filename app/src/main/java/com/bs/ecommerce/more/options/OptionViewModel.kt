package com.bs.ecommerce.more.options

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.options.model.data.AppOptions
import com.bs.ecommerce.utils.Const

class OptionViewModel : BaseViewModel() {

    var optionsLD = MutableLiveData<List<AppOptions>>()

    fun loadOptions() {

        val data = mutableListOf<AppOptions>()

        data.add(
            AppOptions(
                R.string.title_scan_barcode,
                DbHelper.getString(Const.MORE_SCAN_BARCODE),
                R.drawable.ic_scan_barcode
            )
        )

        data.add(
            AppOptions(
                R.string.settings,
                DbHelper.getString(Const.MORE_SETTINGS),
                R.drawable.ic_settings
            )
        )

        data.add(
            AppOptions(
                R.string.privacy_policy,
                DbHelper.getString(Const.MORE_PRIVACY_POLICY),
                R.drawable.ic_privacy
            )
        )

        data.add(
            AppOptions(
                R.string.title_about_us,
                DbHelper.getString(Const.MORE_ABOUT_US),
                R.drawable.ic_about_us
            )
        )

        data.add(
            AppOptions(
                R.string.title_contact_us,
                DbHelper.getString(Const.MORE_CONTACT_US),
                R.drawable.ic_contact_us
            )
        )

        data.add(
            AppOptions(
                R.string.placeholder,
                DbHelper.getString(Const.PRODUCT_VENDOR),
                R.drawable.ic_vendor
            )
        )

        optionsLD.postValue(data)
    }
}