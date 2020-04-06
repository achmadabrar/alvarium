package com.bs.ecommerce.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.LoginFragment
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.utils.*
import java.util.*


abstract class BaseActivity : AppCompatActivity()
{

    val prefObject = PrefSingleton

    protected val isLoggedIn: Boolean
        get() = prefObject.getPrefsBoolValue(PrefSingleton.LOGGED_PREFER_KEY)!!


    protected var viewModel: BaseViewModel? = null

    var ui_hot: TextView? = null

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        //changeMenuItemLoginAction(menu)
        setOrRefreshCurtMenuItem(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)

        = when(item.itemId)
        {
            R.id.menu_cart -> consume { goMenuItemFragment(CartFragment())
        }

        else -> super.onOptionsItemSelected(item)

    }


    fun setOrRefreshCurtMenuItem(menu: Menu)
    {
        val menu_hotlist = menu.findItem(R.id.menu_cart).actionView
        ui_hot = menu_hotlist.findViewById<View>(R.id.hotlist_hot) as TextView
        menu_hotlist.setOnClickListener {

            //closeLeftDrawer()

            if (supportFragmentManager.findFragmentById(R.id.container) !is CartFragment)
                goMenuItemFragment(CartFragment())
        }
        updateHotCount(MyApplication.myCartCounter)
    }

    fun updateHotCount(badgeCount: Int)
    {
        if (ui_hot == null) return
        runOnUiThread {
            if (badgeCount == 0)
                ui_hot?.visibility = View.INVISIBLE
            else
            {
                ui_hot?.visibility = View.VISIBLE
                ui_hot?.text = java.lang.Long.toString(badgeCount.toLong())
            }
        }
    }

    fun goMenuItemFragment(fragment: androidx.fragment.app.Fragment)
    {
        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, fragment).addToBackStack(null).commit()

    }

    fun goMenuItemFragmentifloggedIn(fragment: androidx.fragment.app.Fragment)
    {
        var fragment = fragment
        if (!isLoggedIn)
            fragment = LoginFragment()

        goMenuItemFragment(fragment)

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