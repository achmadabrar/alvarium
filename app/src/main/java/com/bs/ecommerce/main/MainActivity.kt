package com.bs.ecommerce.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.auth.login.LoginFragment
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.home.category.CategoryFragment
import com.bs.ecommerce.home.homepage.HomeFragment
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.OptionsFragment
import com.bs.ecommerce.product.SearchFragment
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.createIfNotInBackStack
import com.bs.ecommerce.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity()
{

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    private var mBackPressed: Long = 0
    private val TIME_INTERVAL: Long = 2000
    private var doubleBackToExitPressedOnce = false

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        mainModel = MainModelImpl(applicationContext)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        mainViewModel.getAppSettings(mainModel)
        setLiveDataListeners()

        initNavigationDrawer()
        setBottomNavigation()
        initHomeFragment()

        setBackStackChangeListener()
    }
    private fun setLiveDataListeners()
    {
        mainViewModel.appSettingsLD.observe(this, Observer { settings ->

            setAppSettings(settings)
        })

    }

    /**
     * Changes bottomNavigationView selected item, based on the name of top fragment in backStack
     */
    private fun setBackStackChangeListener() {
        supportFragmentManager.addOnBackStackChangedListener {

            val topFragment = supportFragmentManager.findFragmentById(R.id.layoutFrame)
            var topFragmentName = ""

            if(topFragment!=null)
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
        val toolbarTop: androidx.appcompat.widget.Toolbar = findViewById(R.id.appToolbar)
        setSupportActionBar(toolbarTop)


        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbarTop, R.string.app_name, R.string.app_name)
        {
            override fun onDrawerClosed(drawerView: View)
            {
                super.onDrawerClosed(drawerView)

                supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

                    when(it)
                    {
                        is HomeFragment -> title = getString(R.string.title_home_page)
                        is CartFragment -> title = getString(R.string.title_shopping_cart)
                        is OptionsFragment -> title = getString(R.string.title_more)
                        is CustomerInfoFragment -> title = getString(R.string.title_customer_info)
                    }
                }

                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerOpened(drawerView: View)
            {
                super.onDrawerOpened(drawerView)

                title = getString(R.string.title_category)

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

    override fun onBackPressed() {

        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) ->
                drawerLayout.closeDrawer(GravityCompat.START)

            supportFragmentManager.backStackEntryCount > 0 ->
                super.onBackPressed()

            else -> handleAppExit()
        }
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

                createIfNotInBackStack<HomeFragment>(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_categories -> {

                createIfNotInBackStack<CategoryFragment>(CategoryFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_search -> {

                createIfNotInBackStack<SearchFragment>(
                    SearchFragment()
                )
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_account -> {

                if(prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                    createIfNotInBackStack<LoginFragment>(CustomerInfoFragment())
                else
                    createIfNotInBackStack<LoginFragment>(LoginFragment())

                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_more -> {

                createIfNotInBackStack<OptionsFragment>(OptionsFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun closeDrawer() =  drawerLayout.closeDrawers()

    private fun initHomeFragment()
    {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutFrame, homeFragment)
            .commit()
    }
}
