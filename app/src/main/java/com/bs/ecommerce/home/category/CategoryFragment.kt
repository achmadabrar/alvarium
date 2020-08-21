package com.bs.ecommerce.home.category

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.catalog.productList.ProductListFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.LeftDrawerItem
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : ToolbarLogoBaseFragment()
{

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    private val itemClickListener: ItemClickListener<LeftDrawerItem> = object : ItemClickListener<LeftDrawerItem> {
        override fun onClick(view: View, position: Int, data: LeftDrawerItem) {

            val activity = requireActivity()

            if(activity is MainActivity) {
                replaceFragmentSafely(
                    ProductListFragment.newInstance(data.name, data.id,
                    ProductListFragment.GetBy.CATEGORY))
            }
        }
    }

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_NAV_CATEGORY)

    override fun getLayoutId(): Int = R.layout.fragment_category

    override fun getRootLayout(): RelativeLayout? = categoryRootLayout

    override fun createViewModel(): MainViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated)
        {
            mainModel = MainModelImpl()

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

    private fun showList(categoryList: List<Category>) {

        val expandableListView: ExpandableListView = expandList
        val adapter = FirstLevelAdapter(requireContext(), categoryList, itemClickListener)
        expandableListView.setAdapter(adapter)
        expandableListView.setOnGroupClickListener { parent, v, groupPosition, _ ->

            val groupIndicator = v.findViewById(R.id.ivExpand) as ImageView

            if (parent.isGroupExpanded(groupPosition)) {
                parent.collapseGroup(groupPosition)
                groupIndicator.setImageResource(R.drawable.ic_plus)
            } else {
                parent.expandGroup(groupPosition)
                groupIndicator.setImageResource(R.drawable.ic_minus)
            }

            adapter.notifyDataSetChanged()
            true
        }

    }
}
