package com.bs.ecommerce.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.utils.MyApplication


open class BaseViewModel : ViewModel()
{
    var isLoadingLD = MutableLiveData<Boolean>()
    var addedToWishListLD = MutableLiveData<Int>()

    var cartLD = MutableLiveData<CartRootData>()

    fun getCartVM(model: CartModel)
    {
        isLoadingLD.value = true

        model.getCartData(object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
    }

    override fun onCleared()
    {
        super.onCleared()
    }

    fun addToWishList(productId: Long) {

        val model = ProductDetailModelImpl()

        model.addProductToWishList(productId, object : RequestCompleteListener<AddToCartResponse> {

            override fun onRequestSuccess(data: AddToCartResponse) {

                if(data.errorList.isNotEmpty()) {
                    toast(data.errorsAsFormattedString)
                    addedToWishListLD.value = 1 // goto cart page
                } else {
                    toast(data.message)
                    addedToWishListLD.value = 2 // success
                }
            }

            override fun onRequestFailed(errorMessage: String) {
                toast(errorMessage)
                addedToWishListLD.value = 3 // other error. do nothing
            }

        })
    }

    protected fun toast(msg: String?) {
        if (MyApplication.mAppContext != null && msg!=null)
            Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT).show()
    }


}