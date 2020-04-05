package com.bs.ecommerce.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.utils.Language
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import java.util.*


abstract class BaseActivity : AppCompatActivity()
{

    val prefObject = PrefSingleton

    protected val isLoggedIn: Boolean
        get() = prefObject.getPrefsBoolValue(PrefSingleton.LOGGED_PREFER_KEY)!!


    protected var viewModel: BaseViewModel? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        viewModel = createViewModel()


        if (Locale.getDefault().language == Language.ARABIC)
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        else
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        setLocale(false)

    }

    protected fun setAppSettings(settings: AppLandingData)
    {
        setAppLanguageFromLanding(settings)
        setAppCurrencyFromLanding(settings)
    }

    private fun setAppLanguageFromLanding(settings: AppLandingData)
    {
        val languageList = settings.languageNavSelector.availableLanguages
        val currentLanguageId = settings.languageNavSelector.currentLanguageId

        var languageToLoad = ""

        for (availableLanguage in languageList)
        {
            if (availableLanguage.id == currentLanguageId)
            {
                "languageSet".showLog("Language got in CategoryNew :: ${currentLanguageId}")

                languageToLoad = when(availableLanguage.name)
                {
                    Language.ENGLISH_AVAILABLE ->  Language.ENGLISH

                    Language.ITALIAN_AVAILABLE ->  Language.ITALIAN

                    Language.ARABIC_AVAILABLE_IN_ENGLISH -> Language.ARABIC

                    Language.ARABIC_AVAILABLE_IN_ARABIC ->  Language.ARABIC

                    else -> ""
                }
            }
        }
        if(languageToLoad.isNotEmpty())
        {
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageToLoad)

            "languageSet".showLog("Language got in ApplandingSettings :: $languageToLoad")

            setLocale(true)
        }
        else
            toast("Error Changing Language")
    }

    private fun setAppCurrencyFromLanding(settings: AppLandingData)
    {
        val currencyList = settings.currencyNavSelector.availableCurrencies
        val currentCurrencyId = settings.currencyNavSelector.currentCurrencyId

        for(currency in currencyList)
        {
            if(currency.id == currentCurrencyId)
                prefObject.setPrefs(PrefSingleton.CURRENT_CURRENCY, currency.name); break

        }
        setLocale(true)
    }

    override fun attachBaseContext(newBase: Context)
    {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context : Context) : Context
    {

        var preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)

        if (TextUtils.isEmpty(preferredLanguage))
        {
            preferredLanguage = Language.ENGLISH
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, preferredLanguage)

        }
        val locale = Locale(preferredLanguage)
        Locale.setDefault(locale)

        val current = Locale.getDefault()
        val currentLanguage = current.language


        if (!currentLanguage.equals(preferredLanguage, ignoreCase = true))
        {
            val preferredLocale = Locale(preferredLanguage)
            Locale.setDefault(preferredLocale)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return updateResourcesLocale(context, locale)
        }

        return updateResourcesLocaleLegacy(context, locale)
    }


    private fun updateResourcesLocale(context : Context, locale : Locale ) : Context
    {
        val configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }


    private fun updateResourcesLocaleLegacy(context : Context, locale : Locale) : Context
    {
        val  resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }



    private fun setLocale(recreate: Boolean)
    {
        var preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)
        if (TextUtils.isEmpty(preferredLanguage)) {
            preferredLanguage = Language.ENGLISH
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, preferredLanguage)
        }

        val current = Locale.getDefault()
        val currentLanguage = current.language


        if (!currentLanguage.equals(preferredLanguage, ignoreCase = true))
        {
            val preferredLocale = Locale(preferredLanguage)
            Locale.setDefault(preferredLocale)

            if (recreate)
                recreate()

        }
    }

}