package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.data.ShippingMethod
import com.bs.ecommerce.customViews.CheckableLinearLayout
import com.bs.ecommerce.customViews.MethodSelectionProcess
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.showLog
import kotlinx.android.synthetic.main.fragment_checkout_step.*
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class ShippingMethodFragment : BaseCheckoutNavigationFragment() {

    private lateinit var methodSelectionProcess: MethodSelectionProcess
    private var shippingMethodValue = ""

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_shipping_method

    override fun getRootLayout(): RelativeLayout = shippingMethodRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        checkoutBottomNav?.menu?.getItem(CheckoutConstants.SHIPPING_TAB)?.isChecked = true

        val shippingMethods = MyApplication.checkoutSaveResponse.data.shippingMethodModel!!.shippingMethods

        addMethodRadioGroup(shippingMethods)


/*        (viewModel as CheckoutViewModel).shippingMethodModelLD.observe(viewLifecycleOwner, Observer { shippingMethodModel ->
            addMethodRadioGroup(shippingMethodModel.shippingMethods)

        })*/

        btnContinue?.setOnClickListener {

            backNavigation = false

            (viewModel as CheckoutViewModel).saveShippingMethodVM(shippingMethodValue, model)
        }

    }
    private fun addMethodRadioGroup(shippingMethods: List<ShippingMethod>?)
    {
        methodSelectionProcess = MethodSelectionProcess(radioGridGroup!!)

        if (shippingMethods != null)
        {
            for (method in shippingMethods)
                generateRadioButton(method)
        }
    }

    private fun generateRadioButton(method: ShippingMethod)
    {
        val eachCheckLayout = layoutInflater.inflate(R.layout.item_shipping_method, radioGridGroup, false) as CheckableLinearLayout

        val description = eachCheckLayout.findViewById<View>(R.id.tv_shippingMethodDescription) as TextView

        val tv_shippingMethodAmount = eachCheckLayout.findViewById<View>(R.id.tv_shippingMethodAmount) as TextView

        val radioButton = eachCheckLayout.findViewById<View>(R.id.rb_shippingChoice) as AppCompatRadioButton

        radioButton.text = method.name
        //radioButton.id = id + 1

        radioButton.id = View.generateViewId()

        tv_shippingMethodAmount.text = method.fee

        if (isPreselected(method))
        {
            radioButton.isChecked = true
            shippingMethodValue = method.name + "___" + method.shippingRateComputationMethodSystemName
        }
        else
            radioButton.isChecked = false

        method.description?.let {  description.text = Html.fromHtml(it) }

        radioGridGroup.addView(eachCheckLayout)

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                shippingMethodValue = method.name + "___" + method.shippingRateComputationMethodSystemName
                Handler().post {  methodSelectionProcess.resetRadioButton(buttonView.id) }
            }
        }

        eachCheckLayout.setOnCheckedChangeListener(object : CheckableLinearLayout.OnCheckedChangeListener
        {
            override fun onCheckedChanged(checkableView: View, isChecked: Boolean)
            {
                if (isChecked)
                    radioButton.isChecked = true

            }
        })
    }
    private fun isPreselected(shippingMethod: ShippingMethod): Boolean = shippingMethod.selected
}