package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.CheckoutStepFragment
import com.bs.ecommerce.more.viewmodel.OptionViewModel
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_options.*
import kotlinx.android.synthetic.main.options_layout.view.*

class OptionsFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_more

    override fun getLayoutId(): Int = R.layout.fragment_options

    override fun getRootLayout(): RelativeLayout? = optionsFragmentRootLayout

    override fun createViewModel(): BaseViewModel = OptionViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            viewModel = ViewModelProvider(this).get(OptionViewModel::class.java)
            (viewModel as OptionViewModel).loadOptions(prefObject)
        }

        observeLiveData()

    }

    private fun observeLiveData() {

        (viewModel as OptionViewModel).optionsLD.observe(viewLifecycleOwner, Observer { options ->
            ll_option_holder.removeAllViews()

            for (i in options) {
                val optionView = layoutInflater.inflate(R.layout.options_layout, null)
                optionView.tag = i.nameResId

                optionView.tvOptionName.text = getString(i.nameResId)
                optionView.ivOptionIcon.setImageResource(i.iconResId)

                optionView.setOnClickListener {

                    when (i.nameResId) {

                        R.string.title_scan_barcode ->
                            checkPermissionAndOpenScannerFragment()

                        R.string.settings ->
                            replaceFragmentSafely(SettingsFragment())

                        R.string.privacy_policy ->
                            replaceFragmentSafely(PrivacyPolicyFragment())

                        R.string.title_about_us ->
                            replaceFragmentSafely(AboutUsFragment())

                        R.string.title_contact_us ->
                            replaceFragmentSafely(ContactUsFragment())

                        R.string.title_checkout ->
                            replaceFragmentSafely(CheckoutStepFragment())

                        R.string.placeholder ->
                            replaceFragmentSafely(ProductDetailFragment.newInstance(40))
                    }
                }

                ll_option_holder.addView(optionView)
            }
        })
    }
}