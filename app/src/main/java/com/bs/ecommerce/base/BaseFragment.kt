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
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.customViews.ContentLoadingDialog
import com.bs.ecommerce.more.barcode.BarCodeCaptureFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.*
import com.pnikosis.materialishprogress.ProgressWheel


abstract class BaseFragment : Fragment()
{
    private var progressWheel: ProgressWheel? = null
    protected open lateinit var viewModel: BaseViewModel
    protected val blockingLoader: ContentLoadingDialog by lazy { ContentLoadingDialog(requireContext()) }

    protected var prefObject = PrefSingleton

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getRootLayout(): RelativeLayout?

    abstract fun createViewModel(): BaseViewModel

    abstract fun getFragmentTitle(): Int

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        setHasOptionsMenu(false)

    }

    private var rootView: View? = null
    protected var viewCreated: Boolean = false
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false)
            viewCreated = false
        } else {
            (rootView?.parent as ViewGroup?)?.removeView(rootView)
            viewCreated = true
        }

        return rootView
    }

    fun setCustomToolbarTitle(title: String) {
        activity?.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

            if (it is BaseFragment)
                "currentFragment".showLog(it.toString())
        }

    }

    protected fun updateCartItemCounter(cartList: List<CartProduct>) : Int
    {
        var totalItems = 0

        for(i in cartList.indices)
        {
            totalItems += cartList[i].quantity
        }
        updateTopCart(totalItems)

        return totalItems
    }

    protected fun updateTopCart(totalItems: Int)
    {
        MyApplication.setCartCounter(totalItems)
        activity?.let {  (it as BaseActivity).updateHotCount(totalItems)    }
    }


    open fun addProductToWishList(product: ProductSummary) {

        if (product.id == null) return

        blockingLoader.showDialog()
        viewModel.addToWishList(product)
    }


    protected fun showLoading()
    {
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        if(progressWheel == null)
            progressWheel = activity?.layoutInflater?.inflate(R.layout.materialish_progressbar, null) as ProgressWheel?

        progressWheel?.spin()

        try {
            getRootLayout()?.addView(progressWheel, params)
        } catch (e: Exception) {
            "exception".showLog(e.toString())
        }
    }

    protected fun hideLoading() = progressWheel?.stopSpinning()



    fun checkPermissionAndOpenScannerFragment()
    {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA), BARCODE_CAMERA_PERMISSION)
        }
        else
        {
            replaceFragmentSafely(BarCodeCaptureFragment())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        when (requestCode)
        {
            BARCODE_CAMERA_PERMISSION ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    replaceFragmentSafely(BarCodeCaptureFragment())
                }
                else
                    toast("Please grant camera permission to use the Barcode Scanner")

                return
            }
        }
    }

    fun logoutConfirmationDialog(onClickListener: View.OnClickListener)
    {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(R.string.are_you_sure_logout).setTitle(R.string.log_out)

        builder.setPositiveButton(R.string.yes) { _, _ ->
            performLogout()

            // update UI
            onClickListener.onClick(rootView)
        }
        builder.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout()
    {
        NetworkUtil.token = ""
        MyApplication.myCartCounter = 0
        prefObject.setPrefs(PrefSingleton.TOKEN_KEY, "")
        prefObject.setPrefs(PrefSingleton.IS_LOGGED_IN, false)
        prefObject.setCustomerInfo(PrefSingleton.CUSTOMER_INFO, null)
        //LoginManager.getInstance().logOut()
        requireActivity().invalidateOptionsMenu()
    }


    private val BARCODE_CAMERA_PERMISSION = 1

}