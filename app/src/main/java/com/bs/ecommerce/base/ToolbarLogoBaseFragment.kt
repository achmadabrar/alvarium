package com.bs.ecommerce.base

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.CartViewModel
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.customViews.ContentLoadingDialog
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.more.barcode.BarCodeCaptureFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.*
import com.pnikosis.materialishprogress.ProgressWheel


abstract class ToolbarLogoBaseFragment : BaseFragment()
{
    override fun onStop() {
        super.onStop()
        hideTopLogo()
    }

    override fun onResume() {
        super.onResume()
        showTopLogo()
    }


    protected fun hideTopLogo() {
        (activity as MainActivity).toolbarTop.logo = null
    }

    protected fun showTopLogo() {
        activity?.title = ""
        (activity as MainActivity).toolbarTop.setLogo(R.drawable.ic_nopstation_logo)
    }

}