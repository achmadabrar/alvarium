package com.bs.ecommerce.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.utils.toast
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.category_left.*
import kotlinx.android.synthetic.main.category_left.categoryRootLayout
import kotlinx.android.synthetic.main.fragment_home.*


class CategoryFragment : BaseFragment()
{

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel


    override fun getLayoutId(): Int = R.layout.category_left

    override fun getRootLayout(): RelativeLayout = categoryRootLayout

    override fun createViewModel(): MainViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getCategoryList(mainModel)


        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {

        mainViewModel.allCategoriesLD.observe(activity!!, Observer { categoryList ->

            toast(categoryList[0].name)
        })


        mainViewModel.isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                progressBarCategory.visibility = View.VISIBLE
            else
                progressBarCategory.visibility = View.GONE
        })




    }


}
