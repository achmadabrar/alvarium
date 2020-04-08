package com.bs.ecommerce.home.homepage

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.HomePageModelImpl
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.category_left.categoryRootLayout


class HomeFragment : BaseFragment()
{

    private lateinit var model: HomePageModel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout? = categoryRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = HomePageModelImpl(activity?.applicationContext!!)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (viewModel as MainViewModel).getFeaturedProducts(model)


        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {

        (viewModel as MainViewModel).homePageProductListLD.observe(activity!!, Observer { homePageProductList ->

            toast(homePageProductList[1].name)
        })


        (viewModel as MainViewModel).isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })




    }



}