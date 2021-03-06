package com.bs.ecommerce.more.options

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.more.contactus.ContactUsFragment
import com.bs.ecommerce.more.settings.SettingsFragment
import com.bs.ecommerce.more.topic.TopicFragment
import com.bs.ecommerce.more.vendor.VendorListFragment
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_options.*
import kotlinx.android.synthetic.main.options_layout.view.*

class OptionsFragment : ToolbarLogoBaseFragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_NAV_MORE)

    override fun getLayoutId(): Int = R.layout.fragment_options

    override fun getRootLayout(): RelativeLayout? = optionsFragmentRootLayout

    override fun createViewModel(): BaseViewModel =
        OptionViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            viewModel = ViewModelProvider(this).get(OptionViewModel::class.java)
            (viewModel as OptionViewModel).loadOptions()

            mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        }

        observeLiveData()

    }

    private fun observeLiveData() {

        (viewModel as OptionViewModel).optionsLD.observe(viewLifecycleOwner, Observer { options ->
            ll_option_holder.removeAllViews()

            val showAllVendors = mainViewModel.appSettingsLD.value?.peekContent()?.showAllVendors == true

            for (i in options) {

                // show/hide based on flag on appLandingSettings
                if(i.nameResId == R.string.placeholder && !showAllVendors)
                    continue

                val optionView = layoutInflater.inflate(R.layout.options_layout, null)
                optionView.tag = i.nameResId

                optionView.tvOptionName.text = i.nameString
                optionView.ivOptionIcon.setImageResource(i.iconResId)

                optionView.setOnClickListener {

                    when (i.nameResId) {

                        R.string.title_scan_barcode ->
                            checkPermissionAndOpenScannerFragment()

                        R.string.settings ->
                            replaceFragmentSafely(SettingsFragment())

                        R.string.privacy_policy ->
                            replaceFragmentSafely(TopicFragment.newInstance(Api.topicPrivacyPolicy))

                        R.string.title_about_us ->
                            replaceFragmentSafely(TopicFragment.newInstance(Api.topicAboutUs))

                        R.string.title_contact_us ->
                            replaceFragmentSafely(ContactUsFragment())

                        R.string.placeholder ->
                            replaceFragmentSafely(VendorListFragment())

                    }
                }

                ll_option_holder.addView(optionView)
            }
        })
    }
}