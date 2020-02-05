package com.bs.ecommerce.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.ui.base.BaseActivity

class MainActivity : BaseActivity()
{


    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        mainModel = MainModelImpl(applicationContext)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getCategoryList(1212, mainModel)

    }
}
