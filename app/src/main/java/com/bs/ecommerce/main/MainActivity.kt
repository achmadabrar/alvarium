package com.bs.ecommerce.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.ui.base.BaseActivity
import com.bs.ecommerce.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity()
{

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        mainModel = MainModelImpl(applicationContext)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getCategoryList(1212, mainModel)



        initNavigationDrawer()
        setBottomNavigation()
        initHomeFragment()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
        (toggle as ActionBarDrawerToggle).syncState()

    }

    override fun onBackPressed()
    {
        when
        {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(
                GravityCompat.START)

            else -> super.onBackPressed()
        }
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

                //supportFragmentManager.beginTransaction().replace(R.id.layout_frame, HomeFragment()).addToBackStack(NopHomeFragment::class.java.simpleName).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_categories -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_search -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_account -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_more -> {


                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun initHomeFragment()
    {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.layout_frame, homeFragment)
            .commit()
    }
}
