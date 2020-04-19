package com.bs.ecommerce.home.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
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
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.data.CategoryModel
import com.bs.ecommerce.product.data.Manufacturer
import com.bs.ecommerce.product.data.ProductSummary
import com.bs.ecommerce.product.ui.ProductListFragment
import com.bs.ecommerce.utils.*
import kotlinx.android.synthetic.main.featured_list_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.slider.view.*


class HomeFragment : BaseFragment() {

    private lateinit var model: HomePageModel
    private lateinit var productClickListener: ItemClickListener<ProductSummary>
    private var viewCreated = false
    private var rootView: View? = null

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout? = homePageRootView

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (rootView == null) {
            rootView = container?.inflate(R.layout.fragment_home)
        } /*else {
            val parent = rootView?.parent as ViewGroup?
            parent?.removeView(rootView)
        }*/
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(R.string.title_home_page)

        if (!viewCreated) {
            model = HomePageModelImpl(requireContext())

            viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

            (viewModel as MainViewModel).apply {
                getFeaturedProducts(model)
                getCategoryListWithProducts(model)
                getManufactures(model)
                getBannerImages(model)
                getBestSellingProducts(model)
            }

            initComponents()
            viewCreated = true
        }

        setLiveDataListeners()

    }

    private fun initComponents() {

        productClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {

                if (data.id != null)
                    replaceFragmentSafely(ProductDetailFragment.newInstance(data.id.toLong()))
            }
        }
    }

    private fun setLiveDataListeners() {
        val viewModel = (viewModel as MainViewModel)

        viewModel.featuredProductListLD.observe(viewLifecycleOwner,
            Observer { list ->
                populateFeaturedProductList(list)
            })

        viewModel.manufacturerListLD.observe(viewLifecycleOwner, Observer { list ->
            populateManufacturerList(list)
        })


        viewModel.featuredCategoryLD.observe(viewLifecycleOwner, Observer { list ->
            populateFeaturedCategoryList(list)
        })


        viewModel.bestSellingProductLD.observe(viewLifecycleOwner, Observer { list ->
            populateBestSellingProductList(list)
        })


        viewModel.imageBannerLD.observe(viewLifecycleOwner, Observer { sliderData ->
            populateBanner(sliderData)
        })


        viewModel.toastMessageLD.observe(viewLifecycleOwner, Observer { msg ->
            if (msg.isNotEmpty()) toast(msg)
        })

        viewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })
    }

    private fun populateBanner(sliderData: SliderData) {
        banner
        if (sliderData.isEnabled == false || sliderData.sliders.isNullOrEmpty()) {
            banner.visibility = View.GONE
            return
        }

        banner.visibility = View.VISIBLE
        val detailsSliderAdapter = BannerSliderAdapter(sliderData.sliders)

        banner.view_pager_slider.adapter = detailsSliderAdapter
        banner.view_pager_slider.currentItem = 0

        banner.circle_indicator.apply {
            setViewPager(banner.view_pager_slider)
            pageColor = ContextCompat.getColor(activity!!, R.color.white)
            fillColor = ContextCompat.getColor(activity!!, R.color.darkOrGray)
        }

        // TODO Enable auto scrolling for the slider
        /*var currentPage = 0

        Timer("SettingUp", false).schedule(object: TimerTask() {
            override fun run() {
                banner.view_pager_slider.currentItem = currentPage % sliderData.sliders.size
                currentPage++
            }

        }, 5000, 2000)*/
    }

    private fun populateFeaturedCategoryList(list: List<CategoryModel>) {
        if (list.isNullOrEmpty()) {
            featuredCategoryContainerLayout.visibility = View.GONE
            return
        }

        featuredCategoryContainerLayout?.visibility = View.VISIBLE
        featuredCategoryContainerLayout?.removeAllViews()


        for (featuredCategory in list) {
            if (featuredCategory.products.isNullOrEmpty()) continue

            val linearLayout = layoutInflater.inflate(
                R.layout.featured_list_layout,
                homePageRootView as RelativeLayout, false
            )

            linearLayout.visibility = View.VISIBLE
            linearLayout.tvTitle.text = featuredCategory.name
            linearLayout.btnSeeAll.setOnClickListener {

                if (featuredCategory.id == null) return@setOnClickListener

                replaceFragmentSafely(
                    ProductListFragment.newInstance(
                        featuredCategory.name ?: "",
                        featuredCategory.id,
                        ProductListFragment.GetBy.CATEGORY
                    )
                )
            }

            linearLayout.rvList.apply {
                setHasFixedSize(true)
                addItemDecoration(RecyclerViewMargin(15, 1, false))
                layoutManager = LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                )

                adapter = FeaturedProductAdapter(
                    featuredCategory.products, productClickListener
                )
            }

            featuredCategoryContainerLayout.addView(linearLayout)
        }
    }

    private fun populateFeaturedProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            featuredProductLayout?.visibility = View.GONE
            return
        }

        featuredProductLayout?.visibility = View.VISIBLE
        featuredProductLayout?.tvTitle?.text = getString(R.string.featured_products)
        featuredProductLayout?.btnSeeAll?.setOnClickListener {
            toast("Sell All")
        }

        featuredProductLayout.rvList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, productClickListener)
        }
    }

    private fun populateBestSellingProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            bestSellingLayout.visibility = View.GONE
            return
        }

        bestSellingLayout.visibility = View.VISIBLE
        bestSellingLayout.tvTitle.text = getString(R.string.best_selling)
        bestSellingLayout.btnSeeAll.setOnClickListener {
            toast("Sell All")
        }

        bestSellingLayout.rvList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)

            adapter = FeaturedProductAdapter(list, productClickListener)
        }
    }

    private fun populateManufacturerList(list: List<Manufacturer>) {
        if (list.isNullOrEmpty()) {
            featuredManufacturerLayout?.visibility = View.GONE
            return
        }

        featuredManufacturerLayout?.visibility = View.VISIBLE
        featuredManufacturerLayout?.divider?.visibility = View.INVISIBLE
        featuredManufacturerLayout?.tvTitle?.text = getString(R.string.featured_manufacturer)
        featuredManufacturerLayout?.btnSeeAll?.setOnClickListener {
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
                    if (data.id == null) return

                    replaceFragmentSafely(
                        ProductListFragment.newInstance(
                            data.name ?: "",
                            data.id,
                            ProductListFragment.GetBy.MANUFACTURER
                        )
                    )
                }
            })
        }
    }


}