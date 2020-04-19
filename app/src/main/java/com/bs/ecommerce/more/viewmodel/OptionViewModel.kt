package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.data.AppOptions

class OptionViewModel : BaseViewModel() {

    var optionsLD = MutableLiveData<List<AppOptions>>()

    fun loadOptions() {
        // Generate option items for MoreOptionFragment
        val data = mutableListOf<AppOptions>()

        data.add(
            AppOptions(
                R.string.settings,
                R.drawable.ic_settings
            )
        )
        data.add(
            AppOptions(
                R.string.privacy_policy,
                R.drawable.ic_privacy
            )
        )
        data.add(
            AppOptions(
                R.string.my_orders,
                R.drawable.ic_my_order
            )
        )
        data.add(
            AppOptions(
                R.string.login,
                R.drawable.ic_login
            )
        )

        optionsLD.postValue(data)
    }
}