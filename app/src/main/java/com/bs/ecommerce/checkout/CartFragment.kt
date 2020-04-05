package com.bs.ecommerce.checkout

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager

import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CartProduct
import com.bs.ecommerce.checkout.model.CartViewModel
import com.bs.ecommerce.checkout.model.PictureModel
import kotlinx.android.synthetic.main.fragment_cart.*


class CartFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_cart

    override fun getRootLayout(): RelativeLayout = cartRootLayout

    override fun createViewModel(): BaseViewModel = CartViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnCheckOut.setOnClickListener{
            showCheckOutOptionsDialogFragment()
        }

        val cartProductList: MutableList<CartProduct> = ArrayList()
        for(i in 1..3) {
            val tmp = CartProduct()
            tmp.productName = "Product Name $i"
            tmp.productId = i
            tmp.quantity = i
            tmp.unitPrice = (i * 15).toString()
            tmp.picture = PictureModel().apply { imageUrl = "https://placeimg.com/300/300/any" }

            cartProductList.add(tmp)
        }

        val cartAdapter = CartAdapter(
            cartProductList,
            this, (requireActivity() as BaseActivity).prefObject
        )


        cartproductRecyclerList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    private fun showCheckOutOptionsDialogFragment() {
        val newFragment = GuestCheckoutFragment()
        newFragment.show(requireActivity().supportFragmentManager, "dialog")
    }

}
