package com.bs.ecommerce.auth.login

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.RegisterFragment
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.main.model.AuthModelImpl
import com.bs.ecommerce.utils.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment()
{

    private lateinit var model: AuthModel

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun getRootLayout(): RelativeLayout = login_root_layout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = AuthModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(LoginViewModel::class.java)

        (viewModel as LoginViewModel).postLoginInfo(LoginPostData(), model)


        setLiveDataListeners()


        initView()
    }

    private fun setLiveDataListeners() {

        (viewModel as LoginViewModel).loginResponseLD.observe(activity!!, Observer { loginResponse ->

            toast(loginResponse.token)
        })

        (viewModel as LoginViewModel).isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }

    private fun initView() {

        Picasso.with(activity)
            .load(R.drawable.app_splash_background)
            .fit()
            .into(backgroundImageView)

        tvNewCustomer.setOnClickListener {

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutFrame,
                    RegisterFragment()
                )
                .addToBackStack(RegisterFragment::class.java.simpleName)
                .commit()
        }
    }

}
