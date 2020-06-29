package com.bs.ecommerce.cart

import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartProduct
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import java.util.*

class CartViewModel : BaseViewModel()
{

    private var dynamicAttributeUpdated = false

    private var DISCOUNT_KEY = "discountcouponcode"
    private var GIFT_CARD_KEY = "giftcardcouponcode"

    private var REMOVE_DISCOUNT_KEY = "removediscount-"
    private var REMOVE_GIFT_CARD_KEY = "removegiftcard-"

    fun removeItemFromCart(productId: Int?, model: CartModel) {

        productId?.let {
            val keyValuePairList = ArrayList<KeyValuePair>()
            keyValuePairList.add(
                KeyValuePair(
                    key = Api.removeFromCartOrWishList,
                    value = it.toString()
                )
            )

            updateCartData(keyValuePairList, model)
        }
    }

    fun updateQuantity(product: CartProduct, isIncrement: Boolean, model: CartModel) {

        val totalQuantity = product.quantity + if(isIncrement) 1 else -1

        product.id?.let {
            val keyValuePairList = ArrayList<KeyValuePair>()
            keyValuePairList.add(
                KeyValuePair(
                    key = Api.cartItemQuantity.plus(it),
                    value = totalQuantity.toString()
                )
            )

            updateCartData(keyValuePairList, model)
        }
    }

    fun updateCartData(allKeyValueList: List<KeyValuePair>, model: CartModel)
    {

        val body = KeyValueFormData(
            allKeyValueList
        )
        body.formValues = allKeyValueList

        isLoadingLD.value = true

        model.updateCartData(body, object : RequestCompleteListener<CartResponse>
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

    fun calculateCostWithUpdatedAttributes(keyValueFormData: KeyValueFormData, model: CartModel) {
        // if(!dynamicAttributeUpdated) return

        isLoadingLD.value = true

        model.applyCheckoutAttributes(keyValueFormData.formValues, object : RequestCompleteListener<CartRootData>
        {
            override fun onRequestSuccess(data: CartRootData)
            {
                isLoadingLD.value = false
                dynamicAttributeUpdated = false

                cartLD.value = data
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
                dynamicAttributeUpdated = false
            }
        })
    }

    fun applyCouponVM(code : String, model: CartModel)
    {
        isLoadingLD.value = true

        model.applyCouponModel(KeyValueFormData(listOf(
            KeyValuePair(
                DISCOUNT_KEY,
                code
            )
        )), object : RequestCompleteListener<CartResponse>
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

    fun removeCouponVM(discountId : Int, code : String, model: CartModel)
    {
        isLoadingLD.value = true

        model.removeCouponModel(KeyValueFormData(listOf(
            KeyValuePair(
                "${REMOVE_DISCOUNT_KEY}$discountId",
                code
            )
        )), object : RequestCompleteListener<CartResponse>
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


    fun applyGiftCardVM(code : String, model: CartModel)
    {
        isLoadingLD.value = true

        model.applyGiftCardModel(KeyValueFormData(listOf(
            KeyValuePair(
                GIFT_CARD_KEY,
                code
            )
        )), object : RequestCompleteListener<CartResponse>
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
    fun removeGiftCardVM(discountId : Int, code : String, model: CartModel)
    {
        isLoadingLD.value = true

        model.removeGiftCardModel(KeyValueFormData(listOf(
            KeyValuePair(
                "${REMOVE_GIFT_CARD_KEY}$discountId",
                code
            )
        )), object : RequestCompleteListener<CartResponse>
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
}