package com.bs.ecommerce.ui.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.ui.base.BaseFragment
import com.bs.ecommerce.ui.base.BaseViewModel
import com.pnikosis.materialishprogress.ProgressWheel
import kotlinx.android.synthetic.main.category_left.*



abstract class BaseHomeFragment : BaseFragment()
{

    protected var progressLeftSide: ProgressWheel? = null

    //abstract fun handleCategoryResponse(categoryNewResponse: CategoryNewResponse?)

    protected fun callGetCategory()
    {
        setProgressForLeftCategory()

       /* RetroClient.api.newCategory.enqueue(object : Callback<CategoryNewResponse>
        {
            override fun onResponse(call: retrofit2.Call<CategoryNewResponse>, response: retrofit2.Response<CategoryNewResponse>)
            {
                val statusCode = response.code()
                progressLeftSide?.stopSpinning()

                if(statusCode != 200)
                    toast("Server Error")


                response.body()?.let {

                    if(it.statusCode == 200)
                        handleCategoryResponse(response.body())
                    else
                        toast("Server Error")

                }
                response.body()  ?: run {   toast("Error From Server")   }

            }

            override fun onFailure(call: retrofit2.Call<CategoryNewResponse>, t: Throwable) = toast("Error")

        })*/

    }

    private fun setProgressForLeftCategory()
    {
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        if(progressLeftSide == null)
            progressLeftSide = activity?.layoutInflater?.inflate(R.layout.materialish_progressbar, null) as ProgressWheel

        progressLeftSide?.spin()

        //ll_rootLayout?.addView(progressLeftSide, params)
    }

}
class CategoryFragment : BaseHomeFragment()
{


    override fun getLayoutId(): Int = R.layout.category_left

    override fun getRootLayout(): RelativeLayout = categoryRootLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //RetroClient.api.newCategory.enqueue(CustomCB(ll_rootLayout))
        callGetCategory()
    }



/*    override fun handleCategoryResponse(response: CategoryNewResponse?)
    {
        if (response?.count!! > 0)
            MyApplication.setCartCounter(response.count)

        expandList.layoutManager = LinearLayoutManager(activity)
        expandList.adapter = CategoryListAdapter(activity!!, response.data, prefObject, this)

        var languageToLoad = ""

        if(MyApplication.loadCategoryNewFragment)
        {
            if (response.language != null)
            {
                for (availableLanguage in response.language.availableLanguages!!)
                {
                    if (availableLanguage.id == response.language.currentLanguageId)
                    {
                        languageToLoad = when(availableLanguage.name)
                        {
                            Language.ENGLISH_AVAILABLE ->  Language.ENGLISH

                            Language.ITALIAN_AVAILABLE ->  Language.ITALIAN

                            Language.ARABIC_AVAILABLE_IN_ENGLISH -> Language.ARABIC

                            Language.ARABIC_AVAILABLE_IN_ARABIC ->  Language.ARABIC

                            else -> ""
                        }
                    }
                }
                if(languageToLoad.isNotEmpty())
                {
                    prefObject.setPrefs(PrefSingleton.CURRENT_LANGUAGE, languageToLoad)
                    (activity as BaseActivity).setLocale(true)
                }
                else
                    toast("Error Changing Language")

            }
        }


        prefObject.setPrefs(PrefSingleton.taxShow, response.isDisplayTaxInOrderSummary)
        prefObject.setPrefs(PrefSingleton.discuntShow, response.isShowDiscountBox)

    }*/
}
