package com.bs.ecommerce.base

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bs.ecommerce.R
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.ProductDetailModelImpl
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.utils.MyApplication



open class BaseViewModel : ViewModel()
{
    var isLoadingLD = MutableLiveData<Boolean>()

    override fun onCleared()
    {
        super.onCleared()
    }

    fun addToWishList(productId: Long) {

        toast(MyApplication.mAppContext?.getString(R.string.adding_to_wish_list))

        val model = ProductDetailModelImpl()

        model.addProductToWishList(productId, object : RequestCompleteListener<AddToCartResponse> {

            override fun onRequestSuccess(data: AddToCartResponse) {

                if(data.errorList.isNotEmpty())
                    toast(data.errorsAsFormattedString)
                else
                    toast(data.message)
            }

            override fun onRequestFailed(errorMessage: String) {
                toast(errorMessage)
            }

        })
    }

    protected fun toast(msg: String?) {
        if (MyApplication.mAppContext != null && msg!=null)
            Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT).show()
    }


}