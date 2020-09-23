package com.bs.ecommerce.checkout

import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.data.PaymentMethod
import com.bs.ecommerce.customViews.CheckableLinearLayout
import com.bs.ecommerce.customViews.MethodSelectionProcess
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.loadJustImg
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class PaymentMethodFragment : BaseCheckoutNavigationFragment() {


    private lateinit var methodSelectionProcess: MethodSelectionProcess
    private var paymentMethodValue = ""

    override fun getFragmentTitle() = DbHelper.getString(Const.SHOPPING_CART_TITLE)

    override fun getLayoutId(): Int = R.layout.fragment_shipping_method

    override fun getRootLayout(): RelativeLayout = shippingMethodRootLayout

    override fun createViewModel(): BaseViewModel = CheckoutViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        MyApplication.checkoutSaveResponse?.data?.paymentMethodModel?.let {

            // saving appLandingData to memory to reload after order is placed
            val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
            DbHelper.memCache = mainViewModel.appSettingsLD.value?.peekContent()

            with(it)
            {
                if(displayRewardPoints)
                {
                    rewardPointCheckBox.visibility = View.VISIBLE

                    rewardPointCheckBox.text = DbHelper.getString(Const.USE_REWARD_POINTS)
                        .replace("{0}", it.rewardPointsBalance.toString())
                        .replace("{1}", it.rewardPointsAmount.toString())

                }

                addMethodRadioGroup(paymentMethods)

                btnContinue?.setOnClickListener {
                    (viewModel as CheckoutViewModel).savePaymentMethodVM(paymentMethodValue, rewardPointCheckBox.isChecked, model)

                    for(i in paymentMethods.indices)
                    {
                        paymentMethods[i].selected = paymentMethods[i].name == methodName
                    }
                }
            }
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

    private fun setMethodValue(method: PaymentMethod)
    {
        paymentMethodValue = method.paymentMethodSystemName
        methodName = method.name
    }

    private fun generateRadioButton(method: PaymentMethod)
    {
        val eachCheckLayout = layoutInflater.inflate(R.layout.item_payment_method, radioGridGroup, false) as CheckableLinearLayout

        val description = eachCheckLayout.findViewById<View>(R.id.tv_paymentMethodDescription) as TextView

        val radioButton = eachCheckLayout.findViewById<View>(R.id.rb_paymentChoice) as AppCompatRadioButton

        val logo = eachCheckLayout.findViewById<View>(R.id.iv_paymentMethodImage) as ImageView
        logo.loadJustImg(method.logoUrl)

        description.text = method.description

        radioButton.text = method.name
        radioButton.id = View.generateViewId()

        if (isPreselected(method))
        {
            radioButton.isChecked = true
            setMethodValue(method)
        }
        else
            radioButton.isChecked = false

        method.description?.let {  description.text = Html.fromHtml(it) }

        radioGridGroup.addView(eachCheckLayout)

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                paymentMethodValue = method.paymentMethodSystemName
                Handler().post {
                    methodSelectionProcess.resetRadioButton(buttonView.id)
                    setMethodValue(method)
                }
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