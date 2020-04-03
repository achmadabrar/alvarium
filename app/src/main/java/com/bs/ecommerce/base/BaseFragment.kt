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
import com.pnikosis.materialishprogress.ProgressWheel
import kotlin.properties.Delegates



abstract class BaseFragment : Fragment()
{
    protected var progressWheel: ProgressWheel? = null
    protected lateinit var viewModel: BaseViewModel

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getRootLayout(): RelativeLayout

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

        viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it)
                {
                    if(getRootLayout() != null )
                        showLoading(getRootLayout())

                    else hideLoading()
                }
            }
        })
    }


    protected fun showLoading(rootLayout: RelativeLayout)
    {
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        if(progressWheel == null)
            progressWheel = activity?.layoutInflater?.inflate(R.layout.materialish_progressbar, null) as ProgressWheel

        progressWheel?.spin()

        rootLayout.addView(progressWheel, params)
    }

    protected fun hideLoading() = progressWheel?.stopSpinning()

}