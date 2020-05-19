package com.bs.ecommerce.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bs.ecommerce.R
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.model.data.AddToWishListResponse
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.OneTimeEvent


open class BaseViewModel : ViewModel() {
    var isLoadingLD = MutableLiveData<Boolean>()
    var addedToWishListLD = MutableLiveData<OneTimeEvent<Long>?>()

    var cartLD = MutableLiveData<CartRootData>()

    fun getCartVM(model: CartModel) {
        isLoadingLD.value = true

        model.getCartData(object : RequestCompleteListener<CartResponse> {
            override fun onRequestSuccess(data: CartResponse) {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun addToWishList(productId: Long) {

        val model = ProductDetailModelImpl()

        model.addProductToWishList(productId, Api.typeWishList,
            object : RequestCompleteListener<AddToWishListResponse> {

                override fun onRequestSuccess(data: AddToWishListResponse) {

                    if (data.redirectionModel?.redirectToDetailsPage == true) {
                        addedToWishListLD.value = OneTimeEvent(productId) // goto product details page
                    } else {
                        toast(MyApplication.mAppContext?.getString(R.string.succcessfully_added_to_wishlist))
                        addedToWishListLD.value = null // success. do nothing
                    }
                }

                override fun onRequestFailed(errorMessage: String) {
                    toast(errorMessage)
                    addedToWishListLD.value = null // error. do nothing
                }

            })
    }

    protected fun toast(msg: String?) {
        if (MyApplication.mAppContext != null && msg != null)
            Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT).show()
    }


}