package com.bs.ecommerce.home.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.catalog.productList.ProductListFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.main.model.data.Category
import com.bs.ecommerce.main.model.data.SecondSubcategory
import com.bs.ecommerce.main.model.data.Subcategory
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.fragment_category.*


class NavDrawerFragment : BaseFragment() {

    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    private val itemClickListener: ItemClickListener<LeftDrawerItem> =
        object : ItemClickListener<LeftDrawerItem> {
            override fun onClick(view: View, position: Int, data: LeftDrawerItem) {

                val activity = requireActivity()

                if (activity is MainActivity) {
                    activity.closeDrawer()

                    activity.supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )

                    replaceFragmentSafely(
                        ProductListFragment.newInstance(
                            data.name, data.id,
                            ProductListFragment.GetBy.CATEGORY
                        )
                    )
                }
            }
        }

    override fun getFragmentTitle() = DbHelper.getString(Const.HOME_NAV_CATEGORY)

    override fun getLayoutId(): Int = R.layout.fragment_category

    override fun getRootLayout(): RelativeLayout? = categoryRootLayout

    override fun createViewModel(): MainViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            mainModel = MainModelImpl()

            mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            if (mainViewModel.navDrawerCategoriesLD.value.isNullOrEmpty())
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
            Observer { isShowLoader -> if (!isShowLoader) showHideLoader(toShow = false) })
    }

    private fun showList(categoryList: List<Category>) {

        val expandableListView: ExpandableListView = expandList
        val adapter = ParentLevel(requireContext(), categoryList)
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

    inner class SecondLevelExpandableListView(context: Context) :
        ExpandableListView(context) {

        override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int
        ) {
            //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
            val mHeightMeasureSpec: Int = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, mHeightMeasureSpec)
        }
    }

    inner class ParentLevel(
        private val context: Context,
        private val categoryList: List<Category>
    ) : BaseExpandableListAdapter() {

        override fun getChild(groupPosition: Int, childPosition: Int): Subcategory {
            return categoryList[groupPosition].subcategories[childPosition]
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return try {
                categoryList[groupPosition].subcategories[childPosition].categoryId.toLong()
            } catch (e: Exception) {
                0
            }
        }

        override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View {
            val secondLevelELV = SecondLevelExpandableListView(requireContext())
            val adapter = SecondLevelAdapter(context, categoryList[groupPosition].subcategories)
            secondLevelELV.setAdapter(adapter)
            secondLevelELV.setGroupIndicator(null)
            secondLevelELV.divider = null
            secondLevelELV.setChildDivider(null)

            secondLevelELV.setOnGroupClickListener { p, v, gp, _ ->

                val groupIndicator = v.findViewById(R.id.ivExpand) as ImageView

                if (p.isGroupExpanded(gp)) {
                    p.collapseGroup(gp)
                    groupIndicator.setImageResource(R.drawable.ic_plus)
                } else {
                    p.expandGroup(gp)
                    groupIndicator.setImageResource(R.drawable.ic_minus)
                }

                adapter.notifyDataSetChanged()
                true
            }

            return secondLevelELV
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return 1
        }

        override fun getGroup(groupPosition: Int): Any {
            return groupPosition
        }

        override fun getGroupCount(): Int {
            return categoryList.size
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getGroupView(
            groupPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View? {
            var mConvertView = convertView

            if (mConvertView == null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mConvertView = inflater.inflate(R.layout.item_category_tree_level_1, null)

                val text = mConvertView?.findViewById(R.id.tvName) as TextView
                text.text = categoryList[groupPosition].name

                text.setOnClickListener {
                    itemClickListener.onClick(
                        mConvertView, groupPosition, LeftDrawerItem(
                            categoryList[groupPosition].categoryId,
                            categoryList[groupPosition].name,
                            isCategory = true
                        )
                    )
                }

                val thumb = mConvertView.findViewById(R.id.ivThumb) as ImageView
                thumb.loadImg(categoryList[groupPosition].iconUrl)

                val icon = mConvertView.findViewById(R.id.ivExpand) as ImageView

                if (isExpanded) {
                    icon.setImageResource(R.drawable.ic_minus)
                } else {
                    icon.setImageResource(R.drawable.ic_plus)
                }

                icon.visibility = if (categoryList[groupPosition].subcategories.isNullOrEmpty())
                    View.GONE else View.VISIBLE
            }

            return mConvertView
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun isChildSelectable(
            groupPosition: Int,
            childPosition: Int
        ): Boolean {
            return true
        }

    }

    inner class SecondLevelAdapter(
        private val context: Context,
        private val subCatList: List<Subcategory>
    ) :
        BaseExpandableListAdapter() {

        override fun getGroup(groupPosition: Int): Subcategory {
            return subCatList[groupPosition]
        }

        override fun getGroupCount(): Int {
            return subCatList.size
        }

        override fun getGroupId(groupPosition: Int): Long {
            return subCatList[groupPosition].categoryId.toLong()
        }

        override fun getGroupView(
            groupPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View? {
            var mConvertView = convertView
            if (mConvertView == null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mConvertView = inflater.inflate(R.layout.item_category_tree_level_2, null)
                val text =
                    mConvertView!!.findViewById(R.id.tvName) as TextView
                text.text = subCatList[groupPosition].name

                text.setOnClickListener {
                    itemClickListener.onClick(
                        mConvertView, groupPosition, LeftDrawerItem(
                            subCatList[groupPosition].categoryId,
                            subCatList[groupPosition].name,
                            isCategory = false
                        )
                    )
                }

                val thumb = mConvertView.findViewById(R.id.ivThumb) as ImageView
                thumb.loadImg(subCatList[groupPosition].iconUrl)

                val icon = mConvertView.findViewById(R.id.ivExpand) as ImageView

                if (isExpanded) {
                    icon.setImageResource(R.drawable.ic_minus)
                } else {
                    icon.setImageResource(R.drawable.ic_plus)
                }

                icon.visibility = if (subCatList[groupPosition].subcategories.isNullOrEmpty())
                    View.GONE else View.VISIBLE
            }
            return mConvertView
        }

        override fun getChild(groupPosition: Int, childPosition: Int): SecondSubcategory {
            return subCatList[groupPosition].subcategories[childPosition]
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return subCatList[groupPosition].subcategories[childPosition].categoryId.toLong()
        }

        override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup?
        ): View? {
            var mConvertView = convertView
            if (mConvertView == null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mConvertView = inflater.inflate(R.layout.item_category_tree_level_3, null)
                val text =
                    mConvertView!!.findViewById(R.id.tvName) as TextView
                text.text = subCatList[groupPosition].subcategories[childPosition].name


                text.setOnClickListener {
                    itemClickListener.onClick(
                        mConvertView, groupPosition, LeftDrawerItem(
                            subCatList[groupPosition].subcategories[childPosition].categoryId,
                            subCatList[groupPosition].subcategories[childPosition].name,
                            isCategory = false
                        )
                    )
                }
            }
            return mConvertView
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return subCatList[groupPosition].subcategories.size
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun isChildSelectable(
            groupPosition: Int,
            childPosition: Int
        ): Boolean {
            return true
        }

    }
}
