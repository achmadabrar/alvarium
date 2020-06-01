package com.bs.ecommerce.more.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.CurrencyNavSelector
import com.bs.ecommerce.main.model.data.LanguageNavSelector
import com.bs.ecommerce.more.viewmodel.BaseUrlChangeFragment
import com.bs.ecommerce.utils.Language
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.hideKeyboard
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment: BaseUrlChangeFragment() {


    //lateinit var response: GetLanguageResponse

    var currencyGetResponse: CurrencyNavSelector? = null
    var languageGetResponse: LanguageNavSelector? = null



    protected var languageCode: String? = ""
    protected var currencyCode: String? = ""

    override fun getFragmentTitle() = R.string.title_settings


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initView()

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getAppSettings(mainModel)

        setLiveDataListeners()

        testUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  validateForm()  }
        mainUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  keepOldUrl()    }

        textChangeListener()

        enableThemeSwitchOption()

    }

    fun initView() {
        labelChangeUrl.text = DbHelper.getString("nopstation.webapi.settings.nopcommerceurl")
        labelLanguage.text = DbHelper.getString("nopstation.webapi.settings.language")
        labelCurrency.text = DbHelper.getString("nopstation.webapi.settings.currency")
        switchTheme.text = DbHelper.getString("nopstation.webapi.settings.darktheme")

        testUrlBtnFromSettings.text = DbHelper.getString("nopstation.webapi.settings.test")
        mainUrlBtnFromSettings.text = DbHelper.getString("nopstation.webapi.settings.setdefault")
    }

    private fun setLiveDataListeners()
    {
        mainViewModel.appSettingsLD.observe(viewLifecycleOwner, Observer { settings ->

            setLanguageDropdown(settings.peekContent().languageNavSelector)

            setCurrencyDropdown(settings.peekContent().currencyNavSelector)


            languageCardView?.visibility = View.VISIBLE
            currencyCardView?.visibility = View.VISIBLE
        })

    }

    private fun setLanguageDropdown(languageNavSelector: LanguageNavSelector)
    {

        with(languageNavSelector)
        {

            languageGetResponse = this

            val nameList = ArrayList<String>()

            val idList = ArrayList<Int>()

            for(currency in availableLanguages)
            {
                if(currency.id == currentLanguageId)
                    prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, currency.name)

            }

            val currentLanguageName = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)

            nameList.add("Current Language : " +  prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE))

            for (data in availableLanguages)
            {
                if(currentLanguageName != data.name)
                {
                    nameList.add(data.name)
                    idList.add(data.id)
                }

            }
            populateLanguageSpinner(nameList, idList)
        }
    }

    private fun setCurrencyDropdown(currencyNavSelector: CurrencyNavSelector)
    {

        with(currencyNavSelector)
        {

            currencyGetResponse = this

            val nameList = ArrayList<String>()

            val idList = ArrayList<Int>()

            for(currency in availableCurrencies)
            {
                if(currency.id == currentCurrencyId)
                    prefObject.setPrefs(PrefSingleton.CURRENT_CURRENCY, currency.name)

            }

            val currentCurrencyName = prefObject.getPrefs(PrefSingleton.CURRENT_CURRENCY)

            nameList.add("Current Currency : " +  prefObject.getPrefs(PrefSingleton.CURRENT_CURRENCY))

            for (currency in availableCurrencies)
            {
                if(currentCurrencyName != currency.name)
                {
                    nameList.add(currency.name)
                    idList.add(currency.id)
                }

            }
            populateCurrencySpinner(nameList, idList)
        }
    }

    private fun createSpinnerAdapter(nameList: List<String>): ArrayAdapter<String>
    {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, nameList)
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        return dataAdapter
    }

    private fun populateLanguageSpinner(languageNameList: List<String>, languageIdList: ArrayList<Int>)
    {

        changeLanguageSpinner?.adapter = createSpinnerAdapter(languageNameList)

        changeLanguageSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
            {
                if(position != 0)
                {
                    currencyCode = languageNameList[position - 1]

                    val languageId = languageIdList[position - 1]

                    changeLanguage(languageId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

    }

    private fun populateCurrencySpinner(currencyNameList: List<String>, currencyIdList: ArrayList<Int>)
    {

        changeCurrencySpinner?.adapter = createSpinnerAdapter(currencyNameList)

        changeCurrencySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
            {
                if(position != 0)
                {
                    currencyCode = currencyNameList[position - 1]

                    val currencyId = currencyIdList[position - 1]

                    changeCurrency(currencyId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

    }

    private fun changeLanguage(languageId : Int)
    {
        var languageBeChangedTo = ""

        for(languages in languageGetResponse!!.availableLanguages)
        {
            if(languages.id == languageId)
            {
                languageBeChangedTo = when(languages.name)
                {
                    Language.ENGLISH_AVAILABLE ->  Language.ENGLISH

                    Language.ITALIAN_AVAILABLE ->  Language.ITALIAN

                    Language.ARABIC_AVAILABLE_IN_ENGLISH -> Language.ARABIC

                    Language.ARABIC_AVAILABLE_IN_ARABIC ->  Language.ARABIC

                    else -> ""
                }

            }
        }

        if(languageBeChangedTo.isNotEmpty())
        {
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageBeChangedTo)
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, languageId)

            mainViewModel.changeLanguage(languageId, model = mainModel)

            (activity as BaseActivity).setLocale(true)
        }
        else
            toast("Error Changing Language")
        

    }
    private fun changeCurrency(currencyId : Int)
    {
        currencyGetResponse?.let {

            for(currency in it.availableCurrencies)
            {
                if(currency.id == currencyId)
                    prefObject.setPrefs(PrefSingleton.CURRENT_CURRENCY, currency.name); break
            }

            mainViewModel.changeCurrency(currencyId, model = mainModel)

            (activity as BaseActivity).setLocale(true)
        }

    }

    private fun enableThemeSwitchOption() {
        themeSwitchCardView.visibility = View.VISIBLE

        switchTheme.isChecked = PrefSingleton.getPrefsBoolValue(PrefSingleton.IS_DARK_THEME)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            //toast("Current theme: ".plus(if(isChecked) "Dark" else "Light"))

            PrefSingleton.setPrefs(PrefSingleton.IS_DARK_THEME, isChecked)

            // Switch Theme
            //requireActivity().recreate()
            (requireActivity() as MainActivity).resetAppTheme()
        }
    }

}