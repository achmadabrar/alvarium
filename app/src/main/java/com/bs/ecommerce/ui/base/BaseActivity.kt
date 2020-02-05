package com.bs.ecommerce.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import kotlin.properties.Delegates


abstract class BaseActivity : AppCompatActivity()
{
    protected lateinit var viewModel: BaseViewModel

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        viewModel = createViewModel()

    }


    protected fun hideKeyboard() {
        val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS) }
    }


}