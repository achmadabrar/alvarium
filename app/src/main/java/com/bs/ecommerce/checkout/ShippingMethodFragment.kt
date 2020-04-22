package com.bs.ecommerce.checkout

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.customViews.CheckableLinearLayout
import com.bs.ecommerce.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_shipping_method.*

class ShippingMethodFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_shopping_cart

    override fun getLayoutId(): Int = R.layout.fragment_shipping_method

    override fun getRootLayout(): RelativeLayout = shippingMethodRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for(i in 1..3) {
            generateRadioButton(ShippingMethod("Shipping Method $i", "$50", "This is a description"))
        }
    }

    private fun generateRadioButton(method: ShippingMethod) {
        val eachCheckLayout = layoutInflater.inflate(
            R.layout.item_shipping_method,
            radioGridGroup,
            false
        ) as CheckableLinearLayout

        val description =
            eachCheckLayout.findViewById<View>(R.id.tv_shippingMethodDescription) as TextView

        val tv_shippingMethodAmount =
            eachCheckLayout.findViewById<View>(R.id.tv_shippingMethodAmount) as TextView

        val radioButton = eachCheckLayout.findViewById<View>(R.id.rb_shippingChoice) as AppCompatRadioButton

        radioButton.text = method.name
        radioButton.id = id + 1
        tv_shippingMethodAmount.text = method.fee

        radioButton.isChecked = true

        description.text = Html.fromHtml(method.description)

        radioGridGroup.addView(eachCheckLayout)
    }

    data class ShippingMethod(
        val name: String,
        val fee: String,
        val description: String
    )
}