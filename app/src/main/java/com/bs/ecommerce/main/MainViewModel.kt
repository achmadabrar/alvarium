package com.bs.ecommerce.main

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.BaseResponse
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.utils.toast


class MainViewModel  : BaseViewModel()
{

    var allCategoriesLD = MutableLiveData<List<Category>>()
    var allCategoriesFailureLD = MutableLiveData<List<String>>()


    fun getCategoryList(model: MainModel)
    {

        isLoadingLD.postValue(true)

        model.getLeftCategories(object : RequestCompleteListener<CategoryTreeResponse>
        {
            override fun onRequestSuccess(data: CategoryTreeResponse)
            {
                isLoadingLD.postValue(false)

                allCategoriesLD.postValue(data.categoryList)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
                allCategoriesFailureLD.postValue(listOf())
            }
        })
    }

}


