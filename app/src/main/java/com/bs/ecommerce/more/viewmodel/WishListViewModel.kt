package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.model.WishListModel
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.product.model.data.WishListData
import com.bs.ecommerce.product.model.data.WishListResponse
import java.util.*

class WishListViewModel : BaseViewModel() {

    var wishListLD = MutableLiveData<WishListData?>()
    var goToCartLD = MutableLiveData<Boolean>()

    fun getWishList(model: WishListModel) {
        isLoadingLD.value = true

        model.getWishList(object : RequestCompleteListener<WishListResponse> {
            override fun onRequestSuccess(data: WishListResponse) {
                isLoadingLD.value = false

                wishListLD.value = data.wishListData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }

    fun updateCartData(keyValuePairs: List<KeyValuePair>, model: WishListModel) {

        isLoadingLD.value = true

        model.updateWishListData(keyValuePairs, object : RequestCompleteListener<WishListResponse> {
            override fun onRequestSuccess(data: WishListResponse) {
                isLoadingLD.value = false

                wishListLD.value = data.wishListData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }

    fun removeItemFromWishList(itemId: Int?, model: WishListModel) {

        if (itemId == null) return

        val keyValuePairs = ArrayList<KeyValuePair>()
        KeyValuePair().apply {

            this.key = Api.removeFromCartOrWishList
            this.value = itemId.toString()

            keyValuePairs.add(this)
        }

        updateCartData(keyValuePairs, model)
    }

    fun moveItemToCart(itemId: Int?, model: WishListModel) {
        prepareFormValue(itemId, false, model)
    }

    fun moveAllItemsToCart(model: WishListModel) {
        prepareFormValue(-1, true, model)
    }

    private fun prepareFormValue(itemId: Int?, allItems: Boolean = false, model: WishListModel) {

        val keyValuePairs = ArrayList<KeyValuePair>()

        if (allItems) {

            if (wishListLD.value?.items.isNullOrEmpty())
                return

            val items = wishListLD.value?.items!!

            for (item in items) {
                KeyValuePair().apply {
                    this.key = Api.addToCart
                    this.value = item.id.toString()

                    keyValuePairs.add(this)
                }
            }
        } else {

            if (itemId == null)
                return

            KeyValuePair().apply {
                this.key = Api.addToCart
                this.value = itemId.toString()

                keyValuePairs.add(this)
            }
        }

        isLoadingLD.value = true

        model.moveItemsToCart(keyValuePairs, object : RequestCompleteListener<WishListResponse> {
            override fun onRequestSuccess(data: WishListResponse) {
                isLoadingLD.value = false

                if (data.errorList.isNotEmpty()) {
                    toast(data.errorsAsFormattedString)
                    goToCartLD.value = false
                } else {
                    toast(data.message)
                    goToCartLD.value = true
                }
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                goToCartLD.value = false

                toast(errorMessage)

            }
        })
    }
}