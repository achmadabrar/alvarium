package com.bs.ecommerce.base

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.afollestad.materialdialogs.MaterialDialog
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.login.LoginFragment
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.utils.*
import java.util.*


abstract class BaseActivity : AppCompatActivity()
{

    val prefObject = PrefSingleton

    protected var viewModel: BaseViewModel? = null

    private var counterText: TextView? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Nop_Theme_Dark)

        if(savedInstanceState == null)
            resetAppTheme()

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


    private fun setOrRefreshCurtMenuItem(menu: Menu)
    {
        val cartCounterView = menu.findItem(R.id.menu_cart).actionView
        counterText = cartCounterView.findViewById<View>(R.id.counterText) as TextView
        cartCounterView.setOnClickListener {

            //closeLeftDrawer()

            if (supportFragmentManager.findFragmentById(R.id.layoutFrame) !is CartFragment)
                goMenuItemFragment(CartFragment())
        }
        updateHotCount(MyApplication.myCartCounter)
    }

    fun resetAppTheme() {
        if (PrefSingleton.getPrefsBoolValue(PrefSingleton.IS_DARK_THEME))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun updateHotCount(badgeCount: Int)
    {
        if (counterText == null) return
        runOnUiThread {
            if (badgeCount == 0)
                counterText?.visibility = View.INVISIBLE
            else
            {
                counterText?.visibility = View.VISIBLE
                counterText?.text = java.lang.Long.toString(badgeCount.toLong())
            }
        }
    }

    fun goMenuItemFragment(fragment: androidx.fragment.app.Fragment)
    {
        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, fragment).addToBackStack(null).commit()

    }

    fun goMenuItemIfLoggedIn(fragment: androidx.fragment.app.Fragment)
    {
        var fragment = fragment
        if (!prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
            fragment = LoginFragment()

        goMenuItemFragment(fragment)

    }

    fun getBaseUrl()
    {
        if (prefObject.getPrefsBoolValue(PrefSingleton.SHOULD_USE_NEW_URL))
            NetworkConstants.BASE_URL = prefObject.getPrefs(PrefSingleton.NEW_URL)

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
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, currentLanguageId)

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
//        super.attachBaseContext(Restring.wrapContext(newBase))
    }

    private fun updateBaseContextLocale(context : Context) : Context
    {
        var preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)

        if (TextUtils.isEmpty(preferredLanguage))
        {
            preferredLanguage = Language.ENGLISH
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, preferredLanguage)
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, 1)
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

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else {
            updateResourcesLocaleLegacy(context, locale)
        }

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



    fun setLocale(recreate: Boolean)
    {
        var preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)
        if (TextUtils.isEmpty(preferredLanguage)) {
            preferredLanguage = Language.ENGLISH
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, preferredLanguage)
            prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, 1)
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

    fun showOrderCompleteDialog(orderId: Int = 0, canHaveOrderId: Boolean = true)
    {
        var msg = ""

        if(canHaveOrderId)
            msg =  "${DbHelper.getString(Const.ORDER_PROCESSED)}\n${DbHelper.getString(Const.ORDER_NUMBER)}: $orderId"
        else
            msg = DbHelper.getString(Const.ORDER_PROCESSED)

        MaterialDialog(this).show {

            title(null, DbHelper.getString(Const.THANK_YOU))

            message(null, msg)

            positiveButton(null, DbHelper.getString(Const.CONTINUE)) {
                finish()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }

            cancelable(false)
            cancelOnTouchOutside(false)

        }
    }

}