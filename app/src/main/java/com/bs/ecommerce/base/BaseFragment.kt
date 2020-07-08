package com.bs.ecommerce.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.GiftCardAdapter
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.OrderTotal
import com.bs.ecommerce.catalog.common.ProductSummary
import com.bs.ecommerce.customViews.ContentLoadingDialog
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.barcode.BarCodeCaptureFragment
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.*
import com.facebook.GraphRequest
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.table_order_total.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


abstract class BaseFragment : Fragment()
{
    private var permissionCallback: PermissionCallback? = null
    private var progressWheel: ProgressWheel? = null
    protected open lateinit var viewModel: BaseViewModel
    protected val blockingLoader: ContentLoadingDialog by lazy { ContentLoadingDialog(requireContext()) }

    protected var prefObject = PrefSingleton

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getRootLayout(): RelativeLayout?

    abstract fun createViewModel(): BaseViewModel

    abstract fun getFragmentTitle(): String

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

    protected fun getRootView(): View? = rootView

    fun setCustomToolbarTitle(title: String) {
        activity?.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getFragmentTitle()

        requireActivity().supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

            if (it is BaseFragment)
                "currentFragment".showLog(it.toString())
        }

    }

    protected fun showHideLoader(toShow: Boolean)
    {
        if (toShow)
            showLoading()
        else
            hideLoading()
    }

    protected fun setCurrentCartItemCounterOnTopView()
    {
        val cartModel = CartModelImpl()
        viewModel.getCartVM(cartModel)

        viewModel.cartLD.observe(viewLifecycleOwner, Observer { cartRootData -> updateCartItemCounter(cartRootData.cart.items) })
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
        activity?.let {  (it as BaseActivity).updateBadgeCount(totalItems)    }
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


    fun checkDiskWritePermission(callback: PermissionCallback) {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_PERMISSION)
            this.permissionCallback = callback
        } else {
            callback.onPermissionResponse(isGranted = true)
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

            WRITE_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionCallback?.onPermissionResponse(isGranted = true)
                } else {
                    permissionCallback?.onPermissionResponse(isGranted = false)
                }
            }
        }
    }

    fun logoutConfirmationDialog(onClickListener: View.OnClickListener)
    {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(DbHelper.getString(Const.ACCOUNT_LOGOUT_CONFIRM))
            .setTitle(DbHelper.getString(Const.ACCOUNT_LOGOUT))

        builder.setPositiveButton(DbHelper.getString(Const.COMMON_YES)) { _, _ ->
            performLogout()

            // update UI
            onClickListener.onClick(rootView)
        }
        builder.setNegativeButton(DbHelper.getString(Const.COMMON_NO)) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    protected fun performLogout()
    {
        NetworkUtil.token = ""
        MyApplication.myCartCounter = 0
        prefObject.setPrefs(PrefSingleton.TOKEN_KEY, "")
        prefObject.setPrefs(PrefSingleton.IS_LOGGED_IN, false)
        prefObject.setCustomerInfo(PrefSingleton.CUSTOMER_INFO, null)
        //LoginManager.getInstance().logOut()
        requireActivity().invalidateOptionsMenu()
    }


    protected fun populateOrderTable(orderTotalModel: OrderTotal)
    {
        subTotalKey?.text = DbHelper.getString(Const.SUB_TOTAL)
        shippingKey?.text = DbHelper.getString(Const.SHIPPING)
        taxKey?.text = DbHelper.getString(Const.TAX)
        discountKey?.text = DbHelper.getString(Const.DISCOUNT)
        totalKey?.text = DbHelper.getString(Const.TOTAL)

        with(orderTotalModel)
        {
            tvSubTotal?.text = subTotal
            tvShippingCharge?.text = shipping


            if (displayTax && tax != null)
            {
                if (displayTaxRates)
                    taxRates?.get(0)?.rate?.let { taxKey?.text = "${DbHelper.getString(Const.TAX)} $it%" }

                tvTax?.text = tax
            }
            else
                taxLayout?.visibility = View.GONE

            tvTotal?.text = orderTotal

            if (subTotalDiscount != null)
            {
                discountLayout?.visibility = View.VISIBLE
                tvDiscount?.text = subTotalDiscount
                underDiscountDivider?.visibility = View.VISIBLE
            }
            else
                discountLayout?.visibility = View.GONE

            if (giftCards != null && giftCards!!.isNotEmpty())
            {
                giftCardLayout?.visibility = View.VISIBLE

                underGiftCardDivider?.visibility = View.VISIBLE

                val giftCardAdapter = GiftCardAdapter(activity!!, giftCards!!)
                giftCardRecyclerList?.layoutManager = LinearLayoutManager(activity)
                giftCardRecyclerList?.adapter = giftCardAdapter
            }
            else
                giftCardLayout?.visibility = View.GONE

            orderTotal?.let {

                if (it.isEmpty())
                    tvTotal?.showTextPendingCalculationOnCheckout()
            } ?: tvTotal?.showTextPendingCalculationOnCheckout()


            shipping?.let {

                if (it.isEmpty())
                    tvShippingCharge?.showTextPendingCalculationOnCheckout()
            } ?: tvShippingCharge?.showTextPendingCalculationOnCheckout()

            if (willEarnRewardPoints != null && willEarnRewardPoints != 0)
            {
                pointsLayout?.visibility = View.VISIBLE
                pointsKey?.text = DbHelper.getString(Const.WILL_EARN)
                tvPoints?.text = DbHelper.getStringWithNumber(Const.POINTS, willEarnRewardPoints!!)
                underDiscountDivider?.visibility = View.VISIBLE
            }
            else
                pointsLayout?.visibility = View.GONE
        }
    }
    
    
    protected fun fetchFileFromStorage(data: Intent?) : FileWithMimeType
    {

        var fileWithMimeType : FileWithMimeType? = null

        data?.data?.let {

            blockingLoader.showDialog()

            try {
                val uri: Uri = it
                val contentResolver: ContentResolver = requireActivity().contentResolver

                val mimeType = contentResolver.getType(uri)
                val returnCursor: Cursor? = contentResolver.query(uri, null, null, null, null)

                val nameIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.SIZE)

                returnCursor?.moveToFirst()

                var filename = nameIndex?.let { i -> returnCursor.getString(i) }
                val fileSize = sizeIndex?.let { i -> returnCursor.getString(i) }

                returnCursor?.close()
/*
                val fileSizeInt = if(fileSize?.isNumeric() == true)
                    fileSize.toInt() else 0

                if(fileSizeInt > 2000000) {
                    blockingLoader.hideDialog()
                    toast(DbHelper.getStringWithNumber(Const.COMMON_MAX_FILE_SIZE, "2000000"))
                    return
                }*/


                // File Input Stream gets me file data
                val inputStream: InputStream? = contentResolver.openInputStream(uri)

                val buffer = ByteArray(inputStream?.available() ?: 0)
                inputStream?.read(buffer)

                val extension = filename?.substring(filename.lastIndexOf(".")) ?: "tmp"
                filename = filename?.replace(extension, "")

                val file = File.createTempFile(filename ?: "temp", extension)
                val outStream: OutputStream = FileOutputStream(file)
                outStream.write(buffer)

                fileWithMimeType = FileWithMimeType(file, mimeType)

                blockingLoader.hideDialog()

            } catch (e: Exception) {
                e.printStackTrace()
                blockingLoader.hideDialog()
            }
        }
        
        return fileWithMimeType!!
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ATTRIBUTE_FILE_UPLOAD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val fileInfo = fetchFileFromStorage(data)

            viewModel.uploadFile(fileInfo.file, fileInfo.mimeType)
        }
    }


    private val BARCODE_CAMERA_PERMISSION = 1
    private val WRITE_PERMISSION = 222

    companion object {
        @JvmStatic val ATTRIBUTE_FILE_UPLOAD_REQUEST_CODE = 500
    }



}

data class FileWithMimeType(val file: File, val mimeType: String?)