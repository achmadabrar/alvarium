package com.bs.ecommerce.home.category

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.product.ProductListFragment
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.category_left.*


class NavDrawerFragment : BaseFragment()
{

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    private val itemClickListener: ItemClickListener<LeftDrawerItem> = object : ItemClickListener<LeftDrawerItem> {
        override fun onClick(view: View, position: Int, data: LeftDrawerItem) {

            val activity = requireActivity()

            if(activity is MainActivity) {
                activity.closeDrawer()

                activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                replaceFragmentSafely(ProductListFragment.newInstance(data.name, data.id,
                    ProductListFragment.GetBy.CATEGORY))
            }
        }
    }

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_NAV_CATEGORY)

    override fun getLayoutId(): Int = R.layout.category_left

    override fun getRootLayout(): RelativeLayout? = categoryRootLayout

    override fun createViewModel(): MainViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated)
        {
            mainModel = MainModelImpl(activity?.applicationContext!!)

            mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            if(mainViewModel.navDrawerCategoriesLD.value.isNullOrEmpty())
                mainViewModel.getNavDrawerCategoryList(mainModel)

            categorySwipeRefresh.setOnRefreshListener {
                categorySwipeRefresh.isRefreshing = false
                showHideLoader(toShow = true)
                mainViewModel.getNavDrawerCategoryList(mainModel)
            }
        }

        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {

        mainViewModel.navDrawerCategoriesLD.observe(viewLifecycleOwner, Observer { categoryList ->

            showList(categoryList)
        })

        mainViewModel.isLoadingLD.observe(
            viewLifecycleOwner,
            Observer { isShowLoader -> if(!isShowLoader) showHideLoader(toShow = false) })
    }

    private fun showList(categoryList: List<Category>)
    {
        /*expandList?.layoutManager = LinearLayoutManager(activity)
        expandList?.adapter = CategoryListAdapter(activity!!, categoryList, this)*/

        expandList?.setAdapter(CategoryListAdapterExpandable(categoryList, itemClickListener))
        expandList?.setGroupIndicator(null)

        // code for collapse all group except selected one
        var lastExpandedPosition = -1
        expandList?.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                expandList.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        }
    }
}
