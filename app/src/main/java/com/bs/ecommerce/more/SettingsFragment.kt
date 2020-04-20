package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.viewmodel.BaseUrlChangeFragment
import com.bs.ecommerce.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: BaseUrlChangeFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        testUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  validateForm()  }
        mainUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  keepOldUrl()    }

        textChangeListener()



    }
}