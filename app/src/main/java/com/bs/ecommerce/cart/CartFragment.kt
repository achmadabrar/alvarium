package com.bs.ecommerce.cart

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.cart.model.CartProduct
import com.bs.ecommerce.cart.model.PictureModel
import com.bs.ecommerce.cart.model.data.CartData
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.toast
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.ll_cart_title.*


class CartFragment : BaseFragment() {


    private lateinit var model: CartModel

    override fun getLayoutId(): Int = R.layout.fragment_cart

    override fun getRootLayout(): RelativeLayout? = cartRootLayout

    override fun createViewModel(): BaseViewModel = CartViewModel()
    


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = CartModelImpl(activity?.applicationContext!!)

        viewModel  = ViewModelProvider(this).get(CartViewModel::class.java)

        (viewModel as CartViewModel).getCartData(model)


        setLiveDataListeners()

        initView()

    }


    private fun setLiveDataListeners() {

        (viewModel as CartViewModel).cartLD.observe(activity!!, Observer { cartData ->

            if(cartData.items.isNotEmpty())
            {
                cartRootLayout?.visibility = View.VISIBLE

                tvTotalItem?.text = "${cartData.items.size} Items"
                MyApplication.setCartCounter(cartData.items.size)

                activity?.let {  (it as BaseActivity).updateHotCount(MyApplication.myCartCounter)    }


                setData(cartData)
            }
            else
            {

                toast("cart Is Empty")
                fragmentManager?.popBackStackImmediate()
               MyApplication.setCartCounter(0)

            }

        })

        (viewModel as CartViewModel).isLoadingLD.observe(activity!!, Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

    }


    private fun setData(cartData: CartData)
    {
        //MyApplication.setCartCounter(cartProductListResponse.count)
        cartInfoLinearLayout?.visibility = View.VISIBLE

        populateProductList(cartData.items)

       /* populatedDatainAdapter(it)
        populateViewOfDynamicAttributeLayout(cartProductListResponse)

        populateDiscountAndGiftcard(cartProductListResponse)

        populateDataInOrderTotalLayout(cartProductListResponse.orderTotalResponseModel)*/

        cartPageView?.visibility = View.VISIBLE
        btnCheckOut?.visibility = View.VISIBLE
    }


    private fun populateProductList(items: List<CartProduct>)
    {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        cartproductRecyclerList?.setHasFixedSize(true)
        cartproductRecyclerList?.layoutManager = layoutManager

        val cartAdapter = CartAdapter(items, this, viewModel, model)

        cartproductRecyclerList?.adapter = cartAdapter
            //makeActionOnCartItemClick(cartAdapter)
    }

    private fun initView()
    {
        btnCheckOut.setOnClickListener{
            showCheckOutOptionsDialogFragment()
        }
    }

    private fun showCheckOutOptionsDialogFragment() {
        val newFragment = GuestCheckoutFragment()
        newFragment.show(requireActivity().supportFragmentManager, "dialog")
    }

}