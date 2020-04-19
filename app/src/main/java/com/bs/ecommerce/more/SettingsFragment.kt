package com.bs.ecommerce.more

import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun getRootLayout(): RelativeLayout? = settingsFragmentRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()
}