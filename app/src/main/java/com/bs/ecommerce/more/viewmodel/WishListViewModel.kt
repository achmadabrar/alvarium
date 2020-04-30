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

    var wishListLD = MutableLiveData<WishListData>()

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

        if(itemId==null) return

        val keyValuePairs = ArrayList<KeyValuePair>()
        KeyValuePair().apply {

            this.key = Api.removeFromCartOrWishList
            this.value = itemId.toString()

            keyValuePairs.add(this)
        }

        updateCartData(keyValuePairs, model)
    }

    fun moveItemToCart(itemId: Int?, model: WishListModel) {
        // TODO implement
    }

    fun moveAllItemsToCart(itemId: Int?, model: WishListModel) {
        // TODO implement
    }
}