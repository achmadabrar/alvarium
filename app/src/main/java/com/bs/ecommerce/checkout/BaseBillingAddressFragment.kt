package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_base_billing_adddress.*
import kotlinx.android.synthetic.main.fragment_billing_address.*


class BaseBillingAddressFragment : BaseFragment()
{

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_base_billing_adddress

    override fun getRootLayout(): RelativeLayout = baseBillingRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        billingTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (tab.position == 0) {
                    layoutBillingAddress.visibility = View.VISIBLE
                    layoutShippingAddress.visibility = View.GONE
                } else if (tab.position == 1) {
                    layoutBillingAddress.visibility = View.GONE
                    layoutShippingAddress.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val categories: MutableList<String> = ArrayList()
        categories.add("Automobile")
        categories.add("Business Services")
        categories.add("Computers")
        categories.add("Education")
        categories.add("Personal")
        categories.add("Travel")

        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spSavedAddress.adapter = dataAdapter
    }


}