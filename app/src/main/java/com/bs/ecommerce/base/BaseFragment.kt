package com.bs.ecommerce.base

import android.Manifest
import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.login.LoginViewModel
import com.bs.ecommerce.more.barcode.BarCodeCaptureFragment
import com.bs.ecommerce.more.viewmodel.OptionViewModel
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.*
import com.pnikosis.materialishprogress.ProgressWheel
import kotlin.properties.Delegates



abstract class BaseFragment : Fragment()
{
    protected var progressWheel: ProgressWheel? = null
    protected open lateinit var viewModel: BaseViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getLayoutId(), container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(getFragmentTitle())

        requireActivity().supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

            if (it is BaseFragment)
                "currentFragment".showLog(it.toString())
        }

      /*  viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it)
                {
                    if(getRootLayout() != null )
                        showLoading()

                    else hideLoading()
                }
            }
        })
*/
    }


    protected fun showLoading()
    {

/*        if(progressWheel != null)
            getRootLayout()?.removeAllViews()*/

        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        if(progressWheel == null)
            progressWheel = activity?.layoutInflater?.inflate(R.layout.materialish_progressbar, null) as ProgressWheel?

        progressWheel?.spin()

        try {
            getRootLayout()?.addView(progressWheel, params)
        } catch (e: Exception)
        {
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

    fun logoutConfirmationDialog()
    {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(R.string.are_you_sure_logout).setTitle(R.string.log_out)

        builder.setPositiveButton(R.string.yes) { _, _ -> performLogout() }
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
        //LoginManager.getInstance().logOut()
        requireActivity().invalidateOptionsMenu()

        (viewModel as OptionViewModel).loadOptions(prefObject)
    }


    private val BARCODE_CAMERA_PERMISSION = 1

}