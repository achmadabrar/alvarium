package com.bs.ecommerce.cart

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.cart.model.data.Value
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.AttributeControlType
import java.util.ArrayList
import java.util.HashMap

class CartViewModel : BaseViewModel()
{
    var cartLD = MutableLiveData<CartRootData>()
    var selectedAttrLD = MutableLiveData<MutableMap<Int, MutableList<Value>>>()

    private var dynamicAttributeUpdated = false


    fun getCartVM(model: CartModel)
    {

        isLoadingLD.value = true

        model.getCartData(object : RequestCompleteListener<CartResponse>
        {
            override fun onRequestSuccess(data: CartResponse)
            {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData

                val attrMap = HashMap<Int, MutableList<Value>>()

                // sorting attribute values
                for (attr in data.cartRootData.cart.checkoutAttributes) {
                    attr.values = attr.values.sortedBy { !it.isPreSelected }

                    val list = mutableListOf<Value>()

                    for (value in attr.values) {
                        if (value.isPreSelected) {
                            list.add(value)

                            if (attr.attributeControlType != AttributeControlType.Checkboxes)
                                break
                        }
                    }

                    attrMap[attr.id] = list
                }

                selectedAttrLD.value = attrMap
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
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

    fun calculateCostWithUpdatedAttributes(model: CartModel) {
        if(!dynamicAttributeUpdated) return

        isLoadingLD.value = true

        val keyValuePairs = ArrayList<KeyValuePair>()

        val attrMap = selectedAttrLD.value ?: mutableMapOf()

        for(mapKey in attrMap) {
            for(i in mapKey.value) {
                KeyValuePair().apply {
                    this.key = "checkout_attribute_${mapKey.key}"
                    this.value = i.id.toString()
                    keyValuePairs.add(this)
                }
            }
        }

        model.applyCheckoutAttributes(keyValuePairs, object : RequestCompleteListener<CartRootData>
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

    fun applyCouponVM(discount : AddDiscountPostData, model: CartModel)
    {

        isLoadingLD.value = true

        model.applyCouponModel(discount, object : RequestCompleteListener<CartResponse>
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

    fun applyGiftCardVM(discount : AddDiscountPostData, model: CartModel)
    {

        isLoadingLD.value = true

        model.applyGiftCardModel(discount, object : RequestCompleteListener<CartResponse>
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

    fun setAttrSelected(
        attrId: Int,
        value: Value,
        isSelected: Boolean,
        multipleSelection: Boolean
    ) {
        val attrMap = selectedAttrLD.value!!

        if (isSelected) {
            if (!multipleSelection) attrMap[attrId] = mutableListOf()

            attrMap[attrId]?.add(value)
        } else
            attrMap[attrId]?.remove(value)

        selectedAttrLD.value = attrMap

        dynamicAttributeUpdated = true
    }

    fun isAttrSelected(attrId: Int, value: Value): Boolean {
        return selectedAttrLD.value?.get(attrId)?.contains(value) ?: false
    }
}