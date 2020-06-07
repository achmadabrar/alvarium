package com.bs.ecommerce.more.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.LanguageLoaderViewModel
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.main.model.data.CurrencyNavSelector
import com.bs.ecommerce.main.model.data.LanguageNavSelector
import com.bs.ecommerce.more.viewmodel.BaseUrlChangeFragment
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment: BaseUrlChangeFragment() {


    lateinit var languageViewModel: LanguageLoaderViewModel

    private var currencyGetResponse: CurrencyNavSelector? = null
    private var languageGetResponse: LanguageNavSelector? = null


    private var rtl = false

    private var isLanguageChanged = false


    private var languageId: Int = -1
    private var currencyCode: String? = ""

    override fun getFragmentTitle() = DbHelper.getString(Const.MORE_SETTINGS)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initView()

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        languageViewModel = ViewModelProvider(this).get(LanguageLoaderViewModel::class.java)

        mainViewModel.getAppSettings(mainModel)

        setLiveDataListeners()

        testUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  validateForm()  }
        mainUrlBtnFromSettings?.setOnClickListener{  requireActivity().hideKeyboard();  keepOldUrl()    }

        textChangeListener()

        enableThemeSwitchOption()

    }

    fun initView() {
        // to avoid auto keyboard popup
        focusStealer.requestFocus()

        labelChangeUrl.text = DbHelper.getString(Const.SETTINGS_URL)
        labelLanguage.text = DbHelper.getString(Const.SETTINGS_LANGUAGE)
        labelCurrency.text = DbHelper.getString(Const.SETTINGS_CURRENCY)
        switchTheme.text = DbHelper.getString(Const.SETTINGS_THEME)

        testUrlBtnFromSettings.text = DbHelper.getString(Const.SETTINGS_BTN_TEST)
        mainUrlBtnFromSettings.text = DbHelper.getString(Const.SETTINGS_BTN_SET_DEFAULT)
    }

    private fun setLiveDataListeners()
    {
        mainViewModel.appSettingsLD.observe(viewLifecycleOwner, Observer { settings ->

            settings.peekContent()?.let {

                languageCardView?.visibility = View.VISIBLE
                currencyCardView?.visibility = View.VISIBLE

                if(isLanguageChanged)
                {
                    rtl = it.rtl
                    changeLanguage(languageId, settings)
                }
                else
                {
                    setLanguageDropdown(it.languageNavSelector)
                    setCurrencyDropdown(it.currencyNavSelector)
                }

            }
        })

        languageViewModel.isLanguageLoaded.observe(viewLifecycleOwner, Observer { loaded ->

            if(languageId!=-1 && loaded.getContentIfNotHandled() == true) {
                "lang_".showLog("Download success? ${loaded.peekContent()}")

                mainViewModel.changeLanguage(languageId, model = mainModel)
            }
        })

        mainViewModel.languageChangeSuccessLD.observe(viewLifecycleOwner, Observer { isChanged ->

            if(isChanged)
            {
                isLanguageChanged = true
                mainViewModel.getAppSettings(mainModel)
            }

        })

        mainViewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })

        languageViewModel.showLoader.observe(viewLifecycleOwner, Observer { show ->
            if(show.getContentIfNotHandled() == true) blockingLoader.showDialog()
            else blockingLoader.hideDialog()
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

            nameList.add("Current Language : ${prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)}")

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

            nameList.add("Current Currency : ${prefObject.getPrefs(PrefSingleton.CURRENT_CURRENCY)}")

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

                    languageId = languageIdList[position - 1]

                    // download language strings
                    languageViewModel.downloadLanguage(languageId)
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

    private fun changeLanguage(languageId: Int, appSettings: OneTimeEvent<AppLandingData?>)
    {
        var languageBehaviour = ""

        if(rtl)
            languageBehaviour = Language.ARABIC
        else
            languageBehaviour = Language.ENGLISH


        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageBehaviour)
        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, languageId)

        //(activity as BaseActivity).setLocale(true)

        appSettings.getContentIfNotHandled()?.let {

            it.stringResources = listOf()

            requireActivity().finish()
            startActivity(
                Intent(requireActivity().applicationContext, MainActivity::class.java)
                    .putExtra(MainActivity.KEY_APP_SETTINGS, it)

            )
        }
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