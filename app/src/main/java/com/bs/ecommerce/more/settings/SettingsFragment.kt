package com.bs.ecommerce.more.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
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


    //lateinit var languageViewModel: LanguageLoaderViewModel

    private var currencyGetResponse: CurrencyNavSelector? = null
    private var languageGetResponse: LanguageNavSelector? = null


    private var rtl = false

    private var isLanguageChanged = false
    private var isCurrencyChanged = false


    private var languageId = -1
    private var currencyId = -1

    private var currencyCode: String? = ""

    override fun getFragmentTitle() = DbHelper.getString(Const.MORE_SETTINGS)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initView()

        mainModel = MainModelImpl(activity?.applicationContext!!)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        //languageViewModel = ViewModelProvider(this).get(LanguageLoaderViewModel::class.java)

        //mainViewModel.getAppSettings(mainModel)

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

        with(mainViewModel)
        {
            appSettingsLD.observe(viewLifecycleOwner, Observer { settings ->

                settings.peekContent()?.let {

                    languageCardView?.visibility = View.VISIBLE
                    currencyCardView?.visibility = View.VISIBLE

                    if(isLanguageChanged)
                    {
                        rtl = it.rtl
                        changeLanguageInAppAndRestart(languageId, settings)
                    }
                    else if(isCurrencyChanged)
                        changeCurrencyInAppAndRestart(currencyId, settings)
                    else
                    {
                        setLanguageDropdown(it.languageNavSelector)
                        setCurrencyDropdown(it.currencyNavSelector)
                    }

                }
            })

            /*languageViewModel.isLanguageLoaded.observe(viewLifecycleOwner, Observer { loaded ->

                if(languageId!=-1 && loaded.getContentIfNotHandled() == true) {
                    "lang_".showLog("Download success? ${loaded.peekContent()}")

                    mainViewModel.changeLanguage(languageId, model = mainModel)
                }
            })*/

            languageChangeSuccessLD.observe(viewLifecycleOwner, Observer { isChanged ->
                if(isChanged) {
                    isLanguageChanged = true
                    mainViewModel.getAppSettings(mainModel)
                }
            })
            currencyChangeSuccessLD.observe(viewLifecycleOwner, Observer { isChanged ->
                if(isChanged) {
                    isCurrencyChanged = true
                    mainViewModel.getAppSettings(mainModel)
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if(isShowLoader) blockingLoader.showDialog()
                else blockingLoader.hideDialog()
            })

            /*languageViewModel.showLoader.observe(viewLifecycleOwner, Observer { show ->
                if(show.getContentIfNotHandled() == true) blockingLoader.showDialog()
                else blockingLoader.hideDialog()
            })*/
        }


    }

    private fun setLanguageDropdown(languageNavSelector: LanguageNavSelector)
    {

        with(languageNavSelector)
        {

            languageGetResponse = this

            val nameList = ArrayList<String>()
            val idList = ArrayList<Int>()

            for(language in availableLanguages)
            {
                if(language.id == currentLanguageId)
                    prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, language.name)
            }

            val currentLanguageName = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)
            nameList.add("Current Language : $currentLanguageName")

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
            nameList.add("Current Currency : $currentCurrencyName")

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
                    //languageViewModel.downloadLanguage(languageId)
                    mainViewModel.changeLanguage(languageId, model = mainModel)
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

                    currencyId = currencyIdList[position - 1]

                    mainViewModel.changeCurrency(currencyId, model = mainModel)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

    }

    private fun restartApp(appSettings: OneTimeEvent<AppLandingData?>)
    {
        appSettings.getContentIfNotHandled()?.let {

            it.stringResources = listOf()

            requireActivity().finish()
            startActivity(
                Intent(requireActivity().applicationContext, MainActivity::class.java)
                    .putExtra(MainActivity.KEY_APP_SETTINGS, it)
            )
        }
    }

    private fun changeLanguageInAppAndRestart(languageId: Int, appSettings: OneTimeEvent<AppLandingData?>)
    {
        var languageBehaviour = ""

        if(rtl)
            languageBehaviour = Language.ARABIC
        else
            languageBehaviour = Language.ENGLISH

        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageBehaviour)
        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, languageId)

        restartApp(appSettings)
    }

    private fun changeCurrencyInAppAndRestart(currencyId: Int, appSettings: OneTimeEvent<AppLandingData?>)
    {
        currencyGetResponse?.let {

            for(currency in it.availableCurrencies)
            {
                if(currency.id == currencyId)
                    prefObject.setPrefs(PrefSingleton.CURRENT_CURRENCY, currency.name); break
            }
        }

        restartApp(appSettings)
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