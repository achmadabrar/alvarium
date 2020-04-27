package com.bs.ecommerce.auth.register

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.showLog
import java.util.HashMap


class RegistrationViewModel  : BaseViewModel()
{

    var getRegistrationResponseLD = MutableLiveData<GetRegistrationResponse>()

    var selectedAttrLD = MutableLiveData<MutableMap<Long, MutableList<AttributeControlValue>>>()


    fun getRegistrationVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getRegisterModel(object : RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)

                setCustomerAttributeValues(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }



    private fun setCustomerAttributeValues(data: GetRegistrationResponse)
    {
        val attrMap = HashMap<Long, MutableList<AttributeControlValue>>()

        for(attr in data.data.customerAttributes)
        {
            attr.values = attr.values.sortedBy { !it.isPreSelected }

            val list = mutableListOf<AttributeControlValue>()

            for(value in attr.values)
            {
                if(value.isPreSelected)
                {
                    list.add(value)

                    if(attr.attributeControlType != AttributeControlType.Checkboxes)
                        break
                }
            }

            attrMap[attr.id] = list
        }

        selectedAttrLD.postValue(attrMap)
    }

    fun getCustomerInfoVM(model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.getCustomerInfoModel(object : RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)

                setCustomerAttributeValues(data)

            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    fun postRegisterVM(registerPostData : GetRegistrationResponse, model: AuthModel)
    {

        isLoadingLD.postValue(true)

        model.postRegisterModel(prepareBodyWithCustomerAttributes(registerPostData), object : RequestCompleteListener<GetRegistrationResponse>
        {
            override fun onRequestSuccess(data: GetRegistrationResponse)
            {
                isLoadingLD.postValue(false)

                getRegistrationResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })
    }

    private fun prepareBodyWithCustomerAttributes(registerPostData: GetRegistrationResponse) : GetRegistrationResponse
    {
        val productAttributePrefix = "customer_attribute"

        val allKeyValueList = ArrayList<KeyValuePair>()

        for ((key, valueList) in selectedAttrLD.value!!)
        {
            if(valueList.isNotEmpty())
            {
                val selectedIdList = valueList.map { it.id }

                for(i in selectedIdList.indices)
                {
                    val keyValuePair = KeyValuePair()
                    keyValuePair.key = "${productAttributePrefix}_${key}"
                    keyValuePair.value = selectedIdList[i].toString()
                    allKeyValueList.add(keyValuePair)

                    "key_value".showLog(" Key : $key    values: ${selectedIdList[i]}")
                }
            }
        }

        registerPostData.formValues = allKeyValueList

        "key_value".showLog(allKeyValueList.toString())

        return registerPostData
    }

    fun isAttrSelected(attrId: Long, value: AttributeControlValue) : Boolean {
        return selectedAttrLD.value?.get(attrId)?.contains(value) ?: false
    }

    fun setAttrSelected(attrId: Long, value: AttributeControlValue,
                        isSelected: Boolean, multipleSelection: Boolean) {
        val attrMap = selectedAttrLD.value!!

        if(isSelected)
        {
            if (!multipleSelection) attrMap[attrId] = mutableListOf()

            attrMap[attrId]?.add(value)
        }
        else
            attrMap[attrId]?.remove(value)

        selectedAttrLD.postValue(attrMap)
    }

}


