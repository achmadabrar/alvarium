package com.bs.ecommerce.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.catalog.common.AddToWishListResponse
import com.bs.ecommerce.catalog.common.ProductSummary
import com.bs.ecommerce.catalog.product.model.ProductDetailModelImpl
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.OneTimeEvent


open class BaseViewModel : ViewModel() {
    var isLoadingLD = MutableLiveData<Boolean>()
    var addedToWishListLD = MutableLiveData<OneTimeEvent<ProductSummary>?>()

    var cartLD = MutableLiveData<CartRootData>()


    fun getCartVM(model: CartModel) {
        isLoadingLD.value = true

        model.getCartData(object :
            RequestCompleteListener<CartResponse> {
            override fun onRequestSuccess(data: CartResponse) {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }

    fun addToWishList(product: ProductSummary) {

        val model =
            ProductDetailModelImpl()

        model.addProductToWishList(product.id!!.toLong(), Api.typeWishList,
            object :
                RequestCompleteListener<AddToWishListResponse> {

                override fun onRequestSuccess(data: AddToWishListResponse) {

                    if (data.redirectionModel?.redirectToDetailsPage == true) {
                        addedToWishListLD.value = OneTimeEvent(product) // goto product details page
                    } else {
                        toast(DbHelper.getString(Const.PRODUCT_ADDED_TO_WISHLIST))
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