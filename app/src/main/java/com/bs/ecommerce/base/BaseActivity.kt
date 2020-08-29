package com.bs.ecommerce.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.afollestad.materialdialogs.MaterialDialog
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.login.LoginFragment
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.SplashScreenActivity
import com.bs.ecommerce.main.model.data.AppLandingData
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


abstract class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener
{

    val prefObject = PrefSingleton

    protected var viewModel: BaseViewModel? = null

    private var counterText: TextView? = null

    private lateinit var connectivityReceiver: ConnectivityReceiver
    private var connectivityErrorDialog : Dialog? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContentView(getLayoutId())

        viewModel = createViewModel()


        if (Locale.getDefault().language == Language.ARABIC)
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        else
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        setLocale(false)
        getBaseUrl()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected)
            showInternetDisconnectedDialog()
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }

    private fun showInternetDisconnectedDialog() {
        connectivityErrorDialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        connectivityErrorDialog?.setContentView(R.layout.custom_dialog_internet_disconnect)

        connectivityErrorDialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)


        val tvNoInternet = connectivityErrorDialog?.findViewById<View>(R.id.tvNoInternet) as TextView

        if(Const.NO_INTERNET != DbHelper.getString(Const.NO_INTERNET))
            tvNoInternet.text = DbHelper.getString(Const.NO_INTERNET)

        val buttonSetting = connectivityErrorDialog?.findViewById<View>(R.id.button_setting) as TextView

        if(Const.SETTINGS != DbHelper.getString(Const.SETTINGS))
            buttonSetting.text = DbHelper.getString(Const.SETTINGS)

        val buttonTryAgain = connectivityErrorDialog?.findViewById<View>(R.id.btnTryAgain) as TextView

        if(Const.TRY_AGAIN != DbHelper.getString(Const.TRY_AGAIN))
            buttonTryAgain.text = DbHelper.getString(Const.TRY_AGAIN)

        val tryAgainLayout = connectivityErrorDialog?.findViewById<View>(R.id.linearTryAgain) as LinearLayout

        buttonSetting.setOnClickListener { sentWifiSettings(this) }

        tryAgainLayout.setOnClickListener {

            connectivityErrorDialog?.dismiss()
            // set delay for smooth animation
            val handler = Handler()
            handler.postDelayed({
                startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                finish()
            }, 500)
        }

        connectivityErrorDialog?.show()
    }
    private fun sentWifiSettings(context: Context?) {
        context?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
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

            if (supportFragmentManager.findFragmentById(R.id.layoutFrame) !is CartFragment)
                goMenuItemFragment(CartFragment())
        }
        updateBadgeCount(MyApplication.myCartCounter)
    }

    fun configDayNightTheme() {
        if (PrefSingleton.getPrefsBoolValue(PrefSingleton.IS_DARK_THEME))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun updateBadgeCount(badgeCount: Int)
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
        closeDrawer()

        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragment(fragment)

    }

    fun closeDrawer() =  drawerLayout?.closeDrawers()

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
        val languageId = settings.languageNavSelector.currentLanguageId

        var languageBehaviour = ""

        if(settings.rtl)
            languageBehaviour = Language.ARABIC
        else
            languageBehaviour = Language.ENGLISH


        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageBehaviour)
        prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE_ID, languageId)


        setLocale(true)
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
        val preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)

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
        val configuration = context.resources.configuration
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
        val preferredLanguage = prefObject.getPrefs(PrefSingleton.CURRENT_LANGUAGE)

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

        MyApplication.setCartCounter(0)
        updateBadgeCount(0)

        MaterialDialog(this).show {

            title(null, DbHelper.getString(Const.THANK_YOU))

            message(null, msg)

            positiveButton(null, DbHelper.getString(Const.CONTINUE)) {
                finish()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                MyApplication.navigateFromCheckoutCompleteToHomePage = true
            }

            cancelable(false)
            cancelOnTouchOutside(false)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

            if (it is BaseFragment)
                it.onActivityResult(requestCode, resultCode, data)
        }
    }

}