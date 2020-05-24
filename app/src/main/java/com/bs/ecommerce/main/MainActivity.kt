package com.bs.ecommerce.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.checkout.*
import com.bs.ecommerce.fcm.MessagingService
import com.bs.ecommerce.home.category.CategoryFragment
import com.bs.ecommerce.home.homepage.HomeFragment
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.OptionsFragment
import com.bs.ecommerce.more.UserAccountFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.ProductListFragment
import com.bs.ecommerce.product.SearchFragment
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_checkout_step.*


class MainActivity : PrivacyPolicyDialogActivity()
{

    lateinit var toolbarTop : androidx.appcompat.widget.Toolbar

    private lateinit var mainModel: MainModelImpl
    private lateinit var mainViewModel: MainViewModel

    private var mBackPressed: Long = 0
    private val TIME_INTERVAL: Long = 2000
    private var doubleBackToExitPressedOnce = false

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // load access token from sharedPreferences to memory
        NetworkUtil.token = prefObject.getPrefs(prefObject.TOKEN_KEY)

        mainModel = MainModelImpl(applicationContext)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setFirebaseTopic()

        setNotificationBundleToDynamicLink()

        setLiveDataListeners()

        initNavigationDrawer()
        setBottomNavigation()

        if (savedInstanceState == null) {
            mainViewModel.getAppSettings(mainModel)
            initHomeFragment()
        }

        setBackStackChangeListener()
    }

    private fun setFirebaseTopic()
    {
        if (!prefObject.getPrefsBoolValue(PrefSingleton.FIRST_RUN))
        {
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/all")
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/android")
            prefObject.setPrefs(PrefSingleton.FIRST_RUN, true)
        }
    }
    private fun setNotificationBundleToDynamicLink()
    {
        val bundle = intent.extras

        if (bundle != null)
        {
            val itemType = Integer.valueOf(bundle.getString(MessagingService.ITEM_TYPE, "0"))
            val itemId = Integer.valueOf(bundle.getString(MessagingService.ITEM_ID, "0"))

            if (itemType == MessagingService.ITEM_PRODUCT)
            {
                gotoFragment(ProductDetailFragment.newInstance(itemId.toLong(), ""))
            }
            else if (itemType == MessagingService.ITEM_CATEGORY)
            {
                gotoFragment(ProductListFragment.newInstance("", itemId, ProductListFragment.GetBy.CATEGORY))
            }
        }
    }

    private fun gotoFragment(fragment: androidx.fragment.app.Fragment)

            = supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()

    private fun setLiveDataListeners()
    {
        mainViewModel.appSettingsLD.observe(this, Observer { settings ->

            settings.getContentIfNotHandled()?.let {
                setAppSettings(it)
            }
        })

    }

    /**
     * Changes bottomNavigationView selected item, based on the name of top fragment in backStack
     */
    private fun setBackStackChangeListener() {
        supportFragmentManager.addOnBackStackChangedListener {

            val topFragment = supportFragmentManager.findFragmentById(R.id.layoutFrame)
            var topFragmentName = ""

            if(topFragment != null)
                topFragmentName = topFragment::class.java.simpleName

            var bottomNavPosition = -1

            when(topFragmentName) {
                OptionsFragment::class.java.simpleName -> bottomNavPosition = 4
                CustomerInfoFragment::class.java.simpleName -> bottomNavPosition = 3
                SearchFragment::class.java.simpleName -> bottomNavPosition = 2
                CategoryFragment::class.java.simpleName -> bottomNavPosition = 1
                HomeFragment::class.java.simpleName -> bottomNavPosition = 0
            }

            if(bottomNavPosition >= 0)
                navigation?.menu?.getItem(bottomNavPosition)?.isChecked = true
        }
    }


    private fun initNavigationDrawer()
    {
        toolbarTop = findViewById(R.id.appToolbar)
        setSupportActionBar(toolbarTop)


        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbarTop, R.string.app_name, R.string.app_name)
        {
            override fun onDrawerClosed(drawerView: View)
            {
                super.onDrawerClosed(drawerView)

                /*supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

                    when(it)
                    {
                        is HomeFragment -> title = getString(R.string.title_home_page)
                        is CartFragment -> title = getString(R.string.title_shopping_cart)
                        is OptionsFragment -> title = getString(R.string.title_more)
                        is CustomerInfoFragment -> title = getString(R.string.title_customer_info)
                    }
                }*/

                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerOpened(drawerView: View)
            {
                super.onDrawerOpened(drawerView)

                //title = getString(R.string.title_category)

                invalidateOptionsMenu()
                syncState()
            }

        }

        drawerLayout.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (toggle as ActionBarDrawerToggle).syncState()

        val navFragment = CategoryFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutDrawer, navFragment)
            .commit()

    }

    private fun goBackTo(fragment: BaseCheckoutNavigationFragment)
    {
        BaseCheckoutNavigationFragment.backNavigation = true

        val csFragment = supportFragmentManager.findFragmentByTag(CheckoutStepFragment::class.java.simpleName)

        val transaction = csFragment!!.childFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(R.id.checkoutFragmentHolder, fragment)
        transaction.commit()
        csFragment.childFragmentManager.executePendingTransactions()

    }

    private fun backPressedLogic()
    {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)

            supportFragmentManager.backStackEntryCount > 0 -> super.onBackPressed()

            else -> handleAppExit()
        }
    }
    override fun onBackPressed()
    {

        if (supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is BaseCheckoutNavigationFragment)
        {
            /*"currentFragment".showLog(it.toString())
            when(it)
            {
                is PaymentMethodFragment -> {
                    goBackTo(ShippingMethodFragment())
                    checkoutBottomNav?.menu?.getItem(CheckoutConstants.SHIPPING_TAB)?.isChecked = true
                }
            }*/
        }
        else
            backPressedLogic()



    }

    private fun handleAppExit() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            toast(R.string.back_button_click_msg)
        }


        mBackPressed = System.currentTimeMillis()

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun setBottomNavigation()
    {
        navigation?.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
        navigation?.enableAnimation(false)
        navigation?.enableShiftingMode(false)
        //navigation?.enableItemShiftingMode(false)
        navigation?.setTextSize(11.0f)
        navigation?.setIconSize(28.0f)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bottom_nav_home -> {

                if(shouldNavigate())
                {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.bottom_nav_categories -> {

                if(shouldNavigate())
                {
                    createIfNotInBackStack<CategoryFragment>(CategoryFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            R.id.bottom_nav_search -> {
                if(shouldNavigate())
                {
                    createIfNotInBackStack<SearchFragment>(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            R.id.bottom_nav_account -> {
                if(shouldNavigate())
                {
                    createIfNotInBackStack<UserAccountFragment>(UserAccountFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            R.id.bottom_nav_more -> {
                if(shouldNavigate())
                {
                    createIfNotInBackStack<OptionsFragment>(OptionsFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
        }
        false
    }


    private fun shouldNavigate() : Boolean
    {
        var flag = true
        if (supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is PaymentInfoFragment)
        {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle(R.string.app_name)
            alertDialogBuilder.setMessage(getString(R.string.exit_payment_dialog_query))
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton("Let's exit") { arg0, arg -> arg0.dismiss(); flag = true }
            alertDialogBuilder.setNegativeButton("No") { arg0, arg -> arg0.dismiss(); flag = false }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        return flag
    }

    fun closeDrawer() =  drawerLayout.closeDrawers()

    private fun initHomeFragment()
    {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutFrame, homeFragment)
            .commit()
    }

    fun switchFragmentFromDialog(dialog: DialogFragment, fragment: BaseFragment?) {

        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutFrame, it, it::class.java.simpleName)
                .addToBackStack(it::class.java.simpleName)
                .commit()
        }
        
        dialog.dismiss()
    }
}
