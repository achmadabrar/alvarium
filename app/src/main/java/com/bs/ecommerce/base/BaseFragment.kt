package com.bs.ecommerce.base

import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.showLog
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

        requireActivity().supportFragmentManager.findFragmentById(R.id.layoutFrame)?.let {

            if (it is BaseFragment)
                "currentFragment".showLog(it.toString())
        }

        viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it)
                {
                    if(getRootLayout() != null )
                        showLoading()

                    else hideLoading()
                }
            }
        })
    }


    protected fun showLoading()
    {

        if(progressWheel != null)
            getRootLayout()?.removeAllViews()

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

}