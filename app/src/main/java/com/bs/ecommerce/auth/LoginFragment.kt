package com.bs.ecommerce.ui.home

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun getRootLayout(): RelativeLayout = login_root_layout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        Picasso.with(activity)
            .load(R.drawable.app_splash_background)
            .fit()
            .into(backgroundImageView)

        tvNewCustomer.setOnClickListener {

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutFrame, RegistrationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}
