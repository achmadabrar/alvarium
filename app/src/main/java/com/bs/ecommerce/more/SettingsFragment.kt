package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.viewmodel.BaseUrlChangeFragment
import com.bs.ecommerce.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.ArrayList

class SettingsFragment: BaseUrlChangeFragment() {


    //lateinit var response: GetLanguageResponse

    //lateinit var currencyResponse: GetCurrencyResponse

    protected var languageCode: String? = ""
    protected var currencyCode: String? = ""

    override fun getFragmentTitle() = R.string.title_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        testUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  validateForm()  }
        mainUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  keepOldUrl()    }

        textChangeListener()

        emptyInitializationSpinner()
        //getLanguageApi()

        //getCurrencyApi()

    }


    private fun emptyInitializationSpinner() = setEmptyAdapter(languageSpinner_settings, getString(R.string.language))


    private fun setEmptyAdapter(spinner: Spinner?, hint: String)
    {
       /* val adapter = SpinnerAdapter(activity!!, R.layout.simple_spinner_item_black_color, ArrayList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.addHint(hint)
        spinner?.adapter = adapter*/

    }
}