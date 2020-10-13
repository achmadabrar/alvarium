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
    var addToCartLD = MutableLiveData<OneTimeEvent<ProductSummary>?>()
    var cartCountLD = MutableLiveData<OneTimeEvent<Int>>()

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

    fun addToWishList(product: ProductSummary, cart: Boolean = false) {

        val model = ProductDetailModelImpl()

        model.addProductToWishList(product.id!!.toLong(), if(cart) Api.typeShoppingCart else Api.typeWishList,
            object :
                RequestCompleteListener<AddToWishListResponse> {

                override fun onRequestSuccess(data: AddToWishListResponse) {

                    val cartCount = data.redirectionModel?.totalShoppingCartProducts ?: 0
                    cartCountLD.value = OneTimeEvent(cartCount)

                    if (data.redirectionModel?.redirectToDetailsPage == true) {

                        if(cart) {
                            addToCartLD.value = OneTimeEvent(product) // goto product details page
                        } else {
                            addedToWishListLD.value = OneTimeEvent(product) // goto product details page
                        }
                    } else {
                        if(cart) {
                            toast(DbHelper.getString(Const.PRODUCT_ADDED_TO_CART))
                            addToCartLD.value = null // success. do nothing
                        } else {
                            toast(DbHelper.getString(Const.PRODUCT_ADDED_TO_WISHLIST))
                            addedToWishListLD.value = null // success. do nothing
                        }
                    }
                }

                override fun onRequestFailed(errorMessage: String) {
                    toast(errorMessage)

                    if(cart)
                        addToCartLD.value = null // error. do nothing
                    else
                        addedToWishListLD.value = null // error. do nothing
                }

            })
    }


    protected fun toast(msg: String?) {
        if (MyApplication.mAppContext != null && msg != null)
            Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT).show()
    }


}