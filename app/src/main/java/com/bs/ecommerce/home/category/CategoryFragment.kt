package com.bs.ecommerce.home.category

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.category_left.*
import kotlinx.android.synthetic.main.category_left.categoryRootLayout


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
            showList(categoryList)
        })


        mainViewModel.isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                progressBarCategory?.visibility = View.VISIBLE
            else
                progressBarCategory?.visibility = View.GONE
        })

    }

    private fun showList(categoryList: List<Category>)
    {
        expandList.layoutManager = LinearLayoutManager(activity)
        expandList.adapter = CategoryListAdapter(activity!!, categoryList, this)
    }


}
