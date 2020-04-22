package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatRadioButton
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class PaymentMethodFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_shipping_method

    override fun getRootLayout(): RelativeLayout = shippingMethodRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..4) {
            generateRadioButton(PaymentMethod("Payment $i", "Visa $i"))
        }
    }

    private fun generateRadioButton(method: PaymentMethod) {
        val linearLayout = layoutInflater.inflate(
            R.layout.item_payment_method,
            radioGridGroup,
            false
        ) as LinearLayout

        val radioButton =
            linearLayout.findViewById<View>(R.id.rb_paymentChoice) as AppCompatRadioButton
        radioButton.text = method.name
        radioButton.id = id + 1

        radioButton.isChecked = true
        radioButton.text = method.paymentMethodSystemName


        /*val imageView = linearLayout.findViewById<View>(R.id.iv_paymentMethodImage) as ImageView
        Picasso.with(context).load(R.drawable.ic_payment).into(imageView)*/

        radioGridGroup!!.addView(linearLayout)

        linearLayout.setOnClickListener { radioButton.isChecked = true }

    }

    data class PaymentMethod(
        val name: String,
        val paymentMethodSystemName: String
    )
}