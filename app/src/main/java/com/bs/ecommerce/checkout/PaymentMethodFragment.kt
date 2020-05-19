package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.data.PaymentMethod
import com.bs.ecommerce.customViews.CheckableLinearLayout
import com.bs.ecommerce.customViews.MethodSelectionProcess
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class PaymentMethodFragment : BaseCheckoutNavigationFragment() {


    private lateinit var methodSelectionProcess: MethodSelectionProcess
    private var paymentMethodValue = ""

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_shipping_method

    override fun getRootLayout(): RelativeLayout = shippingMethodRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutAddressViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

/*        (viewModel as CheckoutAddressViewModel).paymentMethodModelLD.observe(viewLifecycleOwner, Observer { paymentMethodModel ->
            addMethodRadioGroup(paymentMethodModel.paymentMethods)
        })*/

        val paymentMethods = MyApplication.checkoutSaveResponse.data.paymentMethodModel.paymentMethods

        //if(!backNavigation)
        addMethodRadioGroup(paymentMethods)


        btnContinue?.setOnClickListener {

            backNavigation = false

            (viewModel as CheckoutAddressViewModel).savePaymentMethodVM(paymentMethodValue, model)
        }
    }

    private fun addMethodRadioGroup(paymentMethods: List<PaymentMethod>?)
    {
        methodSelectionProcess = MethodSelectionProcess(radioGridGroup!!)

        if (paymentMethods != null)
        {
            for (method in paymentMethods)
                generateRadioButton(method)
        }
    }

    private fun generateRadioButton(method: PaymentMethod)
    {
        val eachCheckLayout = layoutInflater.inflate(R.layout.item_payment_method, radioGridGroup, false) as CheckableLinearLayout

        val description = eachCheckLayout.findViewById<View>(R.id.tv_paymentMethodDescription) as TextView

        val radioButton = eachCheckLayout.findViewById<View>(R.id.rb_paymentChoice) as AppCompatRadioButton

        val logo = eachCheckLayout.findViewById<View>(R.id.iv_paymentMethodImage) as ImageView
        logo.loadImg(method.logoUrl)

        description.text = method.description

        radioButton.text = method.name
        radioButton.id = View.generateViewId()

        if (isPreselected(method))
        {
            radioButton.isChecked = true
            paymentMethodValue = method.paymentMethodSystemName
        }
        else
            radioButton.isChecked = false

        //description.text = Html.fromHtml(method.description)

        radioGridGroup.addView(eachCheckLayout)

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                paymentMethodValue = method.paymentMethodSystemName
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
    private fun isPreselected(paymentMethod: PaymentMethod): Boolean = paymentMethod.selected
}