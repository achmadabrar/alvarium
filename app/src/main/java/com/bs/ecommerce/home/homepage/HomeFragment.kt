package com.bs.ecommerce.home.homepage

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.home.ManufacturerListAdapter
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.HomePageModelImpl
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import com.bs.ecommerce.product.data.ProductSummary
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.featured_list_layout.view.*


class HomeFragment : BaseFragment() {

    private lateinit var model: HomePageModel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout? = homePageRootView

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = HomePageModelImpl(activity?.applicationContext!!)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (viewModel as MainViewModel).apply {
            getFeaturedProducts(model)
            getCategoryListWithProducts(model)
            getManufactures(model)
            getBannerImages(model)
        }


        setLiveDataListeners()

    }

    private fun setLiveDataListeners() {
        val viewModel = (viewModel as MainViewModel)

        viewModel.featuredProductListLD.observe(requireActivity(),
            Observer { list ->
                populateFeaturedProductList(list)
            })

        viewModel.manufacturerListLD.observe(requireActivity(), Observer { list ->
            populateManufacturerList(list)
        })


        viewModel.featuredCategoryLD.observe(requireActivity(), Observer { list ->
            populateFeaturedCategoryList(list)
        })


        viewModel.toastMessageLD.observe(requireActivity(), Observer { msg ->
            if (msg.isNotEmpty()) toast(msg)
        })

        viewModel.isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })
    }

    private fun populateFeaturedCategoryList(list: List<CategoryModel>) {
        if (list.isNullOrEmpty()) {
            featuredCategoryContainerLayout.visibility = View.GONE
            return
        }

        featuredCategoryContainerLayout.visibility = View.VISIBLE
        featuredCategoryContainerLayout.removeAllViews()


        for (featuredCategory in list) {
            if (featuredCategory.products.isNullOrEmpty()) continue

            val linearLayout = layoutInflater.inflate(
                R.layout.featured_list_layout,
                homePageRootView as RelativeLayout, false
            )

            linearLayout.visibility = View.VISIBLE
            linearLayout.tvTitle.text = featuredCategory.name
            linearLayout.btnSeeAll.setOnClickListener {
                toast(featuredCategory.name!!)
            }

            linearLayout.rvList.apply {
                setHasFixedSize(true)
                addItemDecoration(RecyclerViewMargin(15, 1, false))
                layoutManager = LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                )

                adapter = FeaturedProductAdapter(
                    featuredCategory.products,
                    object : ItemClickListener<ProductSummary> {
                        override fun onClick(view: View, position: Int, data: ProductSummary) {
                            toast(data.name!!)
                        }
                    })
            }

            featuredCategoryContainerLayout.addView(linearLayout)
        }
    }

    private fun populateFeaturedProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            featuredProductLayout.visibility = View.GONE
            return
        }

        featuredProductLayout.visibility = View.VISIBLE
        featuredProductLayout.tvTitle.text = getString(R.string.featured_products)
        featuredProductLayout.btnSeeAll.setOnClickListener {
            toast("Sell All")
        }

        featuredProductLayout.rvList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, object : ItemClickListener<ProductSummary> {
                override fun onClick(view: View, position: Int, data: ProductSummary) {
                    toast(data.name!!)
                }
            })
        }
    }

    private fun populateManufacturerList(list: List<Manufacturer>) {
        if (list.isNullOrEmpty()) {
            featuredManufacturerLayout.visibility = View.GONE
            return
        }

        featuredManufacturerLayout.visibility = View.VISIBLE
        featuredManufacturerLayout.divider.visibility = View.INVISIBLE
        featuredManufacturerLayout.tvTitle.text = getString(R.string.featured_manufacturer)
        featuredManufacturerLayout.btnSeeAll.setOnClickListener {
            toast("Sell All")
        }

        featuredManufacturerLayout.rvList.apply {
            val layoutManager11 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            layoutManager = layoutManager11

            val decoration = RecyclerViewMargin(15, 1, false)
            addItemDecoration(decoration)

            adapter = ManufacturerListAdapter(list, object : ItemClickListener<Manufacturer> {
                override fun onClick(view: View, position: Int, data: Manufacturer) {
                    toast(data.name!!)
                }
            })
        }
    }


}