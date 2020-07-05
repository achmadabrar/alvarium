package com.bs.ecommerce.main

import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.customerInfo.CustomerInfoFragment
import com.bs.ecommerce.account.auth.register.RegisterFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.cart.CartFragment
import com.bs.ecommerce.checkout.BaseCheckoutNavigationFragment
import com.bs.ecommerce.checkout.CheckoutStepFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.fcm.MessagingService
import com.bs.ecommerce.home.category.CategoryFragment
import com.bs.ecommerce.home.category.NavDrawerFragment
import com.bs.ecommerce.home.homepage.HomeFragment
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.more.options.OptionsFragment
import com.bs.ecommerce.account.orders.OrderDetailsFragment
import com.bs.ecommerce.more.topic.TopicFragment
import com.bs.ecommerce.account.UserAccountFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.catalog.product.ProductDetailFragment
import com.bs.ecommerce.catalog.productList.ProductListFragment
import com.bs.ecommerce.catalog.search.SearchFragment
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.toast


class MainActivity : PrivacyPolicyDialogActivity(), View.OnClickListener
{

    lateinit var toolbarTop : androidx.appcompat.widget.Toolbar
    lateinit var toggle: ActionBarDrawerToggle

    private var mBackPressed: Long = 0
    private val TIME_INTERVAL: Long = 2000
    private var doubleBackToExitPressedOnce = false

    var notificationClicked = false

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

        //setLiveDataListeners()

        initNavigationDrawer()
        setBottomNavigation()

        if (savedInstanceState == null) {

            DbHelper.memCache?.let {
                mainViewModel.setAppSettings(it)
                setAppSettings(it)
                DbHelper.memCache = null

                initHomeFragment()
            } ?: run {
                finish()
                startActivity(Intent(this@MainActivity, SplashScreenActivity::class.java))
            }

            //mainViewModel.getAppSettings(mainModel)
        }

        setBackStackChangeListener()

        setNotificationBundleToDynamicLink(intent)
    }

    /**
     * Called when user clicks a notification while the app is in foreground
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setNotificationBundleToDynamicLink(intent)
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


    private fun setNotificationBundleToDynamicLink(intent: Intent?)
    {
        intent?.extras?.let {

            var itemType: Int
            var itemId: Int

            try {
                itemType = Integer.valueOf(it.getString(MessagingService.ITEM_TYPE, "0"))
                itemId = Integer.valueOf(it.getString(MessagingService.ITEM_ID, "0"))
            } catch (e: NumberFormatException) {
                itemType = 0
                itemId = 0
            }

            when(itemType)
            {
                NotificationType.PRODUCT ->
                    gotoFragment(ProductDetailFragment.newInstance(productId = itemId.toLong(), productName = ""))

                NotificationType.CATEGORY ->
                    gotoFragment(ProductListFragment.newInstance("", categoryId = itemId, getBy = ProductListFragment.GetBy.CATEGORY))

                NotificationType.MANUFACTURER ->
                    gotoFragment(ProductListFragment.newInstance("", categoryId = itemId, getBy = ProductListFragment.GetBy.MANUFACTURER))

                NotificationType.VENDOR ->
                    gotoFragment(ProductListFragment.newInstance("", categoryId = itemId, getBy = ProductListFragment.GetBy.VENDOR))

                NotificationType.ORDER ->
                    gotoFragment(OrderDetailsFragment.newInstance(orderId = itemId))

                NotificationType.ACCOUNT ->
                    gotoFragment(CustomerInfoFragment())

                NotificationType.TOPIC ->
                    gotoFragment(TopicFragment.newInstance(topicId = itemId))

            }
        }
    }

    private fun gotoFragment(fragment: androidx.fragment.app.Fragment)
    {
        supportFragmentManager.beginTransaction().add(R.id.layoutFrame, fragment).addToBackStack(null).commit()
        notificationClicked = true
    }

    /**
     * Changes bottomNavigationView selected item, based on the name of top fragment in backStack
     */
    private fun setBackStackChangeListener() {
        supportFragmentManager.addOnBackStackChangedListener {

            // highlight selected bottom navigation item
            val topFragment = supportFragmentManager.findFragmentById(R.id.layoutFrame)
            var topFragmentName = ""

            if(topFragment != null)
                topFragmentName = topFragment::class.java.simpleName

            var bottomNavPosition = -1

            when(topFragmentName) {
                OptionsFragment::class.java.simpleName -> bottomNavPosition = 4
                UserAccountFragment::class.java.simpleName -> bottomNavPosition = 3
                SearchFragment::class.java.simpleName -> bottomNavPosition = 2
                CategoryFragment::class.java.simpleName -> bottomNavPosition = 1
                HomeFragment::class.java.simpleName -> bottomNavPosition = 0
            }

            if(bottomNavPosition >= 0)
                mainBottomNav?.menu?.getItem(bottomNavPosition)?.isChecked = true

            // set back button
            if (supportFragmentManager.backStackEntryCount > 0) {
                setArrowIconInDrawer()
            } else {
                toggle.isDrawerIndicatorEnabled = true
                setAnimationOnDrawerIcon()
            }

            hideKeyboard()

            // show/hide toolbar logo & title
            val show = when(topFragmentName) {
                HomeFragment::class.java.simpleName -> true
                CategoryFragment::class.java.simpleName -> true
                UserAccountFragment::class.java.simpleName -> true
                OptionsFragment::class.java.simpleName -> true
                CheckoutStepFragment::class.java.simpleName -> true
                BaseCheckoutNavigationFragment::class.java.simpleName -> true
                RegisterFragment::class.java.simpleName -> true
                else -> false
            }

            val toolbarTitle = if(topFragment is BaseFragment) topFragment.getFragmentTitle() else ""

            topLogoLayout?.visibility = if(show) View.VISIBLE else View.GONE
            title = toolbarTitle

            "toolbar_".showLog("logo visible - $show <> title - $toolbarTitle")

        }

        // set back button after theme/configuration change
        if (supportFragmentManager.backStackEntryCount > 0) {
            setArrowIconInDrawer()
        }
    }


    private fun setArrowIconInDrawer()
    {
        toggle.isDrawerIndicatorEnabled = false

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavigationDrawer()
    {
        toolbarTop = findViewById(R.id.appToolbar)
        setSupportActionBar(toolbarTop)
        topLogoLayout?.topLogo?.loadImg(PrefSingleton.getPrefs(PrefSingleton.APP_LOGO), null)


        toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbarTop, R.string.app_name, R.string.app_name)
        {
            override fun onDrawerClosed(drawerView: View)
            {
                super.onDrawerClosed(drawerView)

                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerOpened(drawerView: View)
            {
                super.onDrawerOpened(drawerView)

                invalidateOptionsMenu()
                syncState()
            }

        }

        drawerLayout.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        toolbarTop.setNavigationOnClickListener(this)

        val navFragment = NavDrawerFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutDrawer, navFragment)
            .commit()

    }

    private fun setAnimationOnDrawerIcon()
    {
        val anim = ValueAnimator.ofFloat(1F, 0F)

        anim.addUpdateListener { valueAnimator ->

            val slideOffset = valueAnimator.animatedValue as Float
            toggle.onDrawerSlide(drawerLayout, slideOffset)
        }
        anim.interpolator = DecelerateInterpolator()
        anim.duration = 800
        anim.start()

    }

    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?)
    {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onClick(v: View) = shouldDisplayHomeUp()

    private fun shouldDisplayHomeUp()
    {
        if (supportActionBar != null)
        {
            if (shouldOpenDrawer())
            {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawers()
                else
                    drawerLayout.openDrawer(GravityCompat.START)
            }
            else if (supportFragmentManager.backStackEntryCount > 0)
            {
                setArrowIconInDrawer()
                onBackPressed()
                shouldOpenDrawer()
            }
            else
                toggle.isDrawerIndicatorEnabled = true
        }
    }

    private fun shouldOpenDrawer(): Boolean
    {
        if (supportFragmentManager.backStackEntryCount == 0)
        {
            toggle.isDrawerIndicatorEnabled = true
            return true
        }
        else
            return false
    }

    override fun onBackPressed()
    {
        when {
            supportFragmentManager.findFragmentById(R.id.checkoutFragmentHolder) is BaseCheckoutNavigationFragment ->
            {
                supportFragmentManager.popBackStack(CartFragment::class.java.simpleName, 0)

                //MyApplication.getBillingResponse = null
                //MyApplication.checkoutSaveResponse = null
                MyApplication.previouslySelectedTab = 0
            }


            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)

            supportFragmentManager.backStackEntryCount > 0 -> super.onBackPressed()

            else -> handleAppExit()
        }
    }

    private fun handleAppExit() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            toast(DbHelper.getString(Const.COMMON_AGAIN_PRESS_TO_EXIT))
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
        mainBottomNav?.apply{
            menu.clear()

            menu.add(Menu.NONE, R.id.bottom_nav_home, 0, DbHelper.getString(Const.HOME_NAV_HOME)).setIcon(R.drawable.app_bottom_icon_home)
            menu.add(Menu.NONE, R.id.bottom_nav_categories, 1, DbHelper.getString(Const.HOME_NAV_CATEGORY)).setIcon(R.drawable.app_bottom_icon_categories)
            menu.add(Menu.NONE, R.id.bottom_nav_search, 2, DbHelper.getString(Const.HOME_NAV_SEARCH)).setIcon(R.drawable.app_bottom_icon_search)
            menu.add(Menu.NONE, R.id.bottom_nav_account, 3, DbHelper.getString(Const.HOME_NAV_ACCOUNT)).setIcon(R.drawable.app_bottom_icon_account)
            menu.add(Menu.NONE, R.id.bottom_nav_more, 4, DbHelper.getString(Const.HOME_NAV_MORE)).setIcon(R.drawable.app_bottom_icon_more)

            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bottom_nav_home -> {

                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

                createIfNotInBackStack<UserAccountFragment>(
                    UserAccountFragment()
                )
                return@OnNavigationItemSelectedListener true

            }
            R.id.bottom_nav_more -> {

                createIfNotInBackStack<OptionsFragment>(
                    OptionsFragment()
                )
                return@OnNavigationItemSelectedListener true


            }
        }
        false
    }

    private fun initHomeFragment()
    {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layoutFrame, homeFragment)
            .commit()
    }

    fun switchFragmentFromDialog(dialog: DialogFragment, fragment: BaseFragment?) {

        fragment?.let {
            replaceFragment(fragment)
        }
        
        dialog.dismiss()
    }
}
