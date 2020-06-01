package com.bs.ecommerce.home.homepage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.CartModelImpl
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.home.ManufacturerListAdapter
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.HomePageModelImpl
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.product.ProductListFragment
import com.bs.ecommerce.product.model.data.CategoryModel
import com.bs.ecommerce.product.model.data.Manufacturer
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.product.model.data.SubCategory
import com.bs.ecommerce.utils.*
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.featured_list_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_fragment_bottomsheet.*
import kotlinx.android.synthetic.main.home_fragment_bottomsheet.view.*
import kotlinx.android.synthetic.main.home_page_banner.view.*
import java.net.URL
import kotlin.concurrent.thread


class HomeFragment : ToolbarLogoBaseFragment() {

    private lateinit var model: HomePageModel
    private lateinit var cartModel: CartModel
    private var observeLiveDataChange = true

    private lateinit var productClickListener: ItemClickListener<ProductSummary>

    private val bsBehavior: BottomSheetBehavior<*> by lazy {
        BottomSheetBehavior.from(bottomSheetLayout)
    }


    override fun getFragmentTitle() =  R.string.title_home_page

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout? = homePageRootView

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        if(requireActivity().isOnline())
        {
            callCart()

            if (!viewCreated) {
                model = HomePageModelImpl(requireContext())

                (viewModel as MainViewModel).getAllLandingPageProducts(model)

                initComponents()
                observeLiveDataChange = true
            } else {
                observeLiveDataChange = false
            }

            setLiveDataListeners()
        }
        else
            showInternetDisconnectedDialog()


    }
    private fun callCart()
    {
        cartModel = CartModelImpl()
        (viewModel as MainViewModel).getCartVM(cartModel)
    }

    private fun initComponents() {

        productClickListener = object : ItemClickListener<ProductSummary> {
            override fun onClick(view: View, position: Int, data: ProductSummary) {

                if(data.id == null) return

                when(view.id) {

                    R.id.itemView ->
                        replaceFragmentSafely(ProductDetailFragment.newInstance(data.id.toLong(), data.name))

                    R.id.ivAddToFav ->
                        addProductToWishList(data)
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {

            if(requireActivity().isOnline())
            {
                swipeRefreshLayout.isRefreshing = false

                (viewModel as MainViewModel).apply {
                    observeLiveDataChange = true

                    getAllLandingPageProducts(model)
                }
            }
            else
                showInternetDisconnectedDialog()



        }

        featuredProductLayout?.rvList?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
            setHasFixedSize(true)
        }

        featuredManufacturerLayout?.rvList?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
        }

        bestSellingLayout?.rvList?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

            addItemDecoration(RecyclerViewMargin(15, 1, false))
        }

        tvCloseBs.text = DbHelper.getString("common.done")
        tvCloseBs.setOnClickListener {
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setLiveDataListeners() {
        val viewModel = (viewModel as MainViewModel)

        viewModel.cartLD.observe(viewLifecycleOwner, Observer { cartRootData -> updateCartItemCounter(cartRootData.cart.items) })

        viewModel.featuredProductListLD.observe(viewLifecycleOwner,
            Observer { list ->
                if(observeLiveDataChange) populateFeaturedProductList(list)
            })

        viewModel.manufacturerListLD.observe(viewLifecycleOwner, Observer { list ->
            if(observeLiveDataChange) populateManufacturerList(list)
        })


        viewModel.featuredCategoryLD.observe(viewLifecycleOwner, Observer { list ->
            if(observeLiveDataChange) populateFeaturedCategoryList(list)
        })


        viewModel.bestSellingProductLD.observe(viewLifecycleOwner, Observer { list ->
            if(observeLiveDataChange) populateBestSellingProductList(list)
        })


        viewModel.imageBannerLD.observe(viewLifecycleOwner, Observer { sliderData ->
            if(observeLiveDataChange) populateBanner(sliderData)
        })


        viewModel.homePageLoader.observe(viewLifecycleOwner, Observer { isShowLoader ->
            if (isShowLoader)
                showLoading()
            else
                hideLoading()
        })

        viewModel.addedToWishListLD.observe(viewLifecycleOwner, Observer { action ->

            action?.getContentIfNotHandled()?.let { product ->
                replaceFragmentSafely(ProductDetailFragment.newInstance(
                    product.id?.toLong() ?: -1L, product.name))
            }

            blockingLoader.hideDialog()
        })
    }

    private fun populateBanner(sliderData: SliderData) {

        if (sliderData.isEnabled == false || sliderData.sliders.isNullOrEmpty()) {
            banner?.visibility = View.GONE
            return
        }

        banner?.visibility = View.VISIBLE

        banner?.view_pager_slider1?.apply {
            removeAllSliders()
            setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            setCustomIndicator(view!!.findViewById<View>(R.id.circle_indicator) as PagerIndicator)
        }

        var biggestImageAR = 100000F

        thread {
            for ((i, imageModel) in sliderData.sliders.withIndex()) {

                requireActivity().runOnUiThread {
                    val textSliderView = DefaultSliderView(requireContext())

                    textSliderView.image(imageModel.imageUrl).scaleType =
                        BaseSliderView.ScaleType.CenterInside

                    banner?.view_pager_slider1?.addSlider(textSliderView)
                }

                //Execute all the long running tasks here
                val url = URL(imageModel.imageUrl)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                val thisImageAR = (bitmap?.width!!.toFloat() / bitmap.height.toFloat())

                if (thisImageAR < biggestImageAR)
                    biggestImageAR = thisImageAR

                "banner_ar".showLog("ar of image $i: $thisImageAR, largest ar: $biggestImageAR")
            }

            banner?.slider?.setmAspectRatio(biggestImageAR)
        }

    }

    private fun populateFeaturedCategoryList(list: List<CategoryModel>) {
        if (list.isNullOrEmpty()) {
            featuredCategoryContainerLayout?.visibility = View.GONE
            return
        }

        featuredCategoryContainerLayout?.visibility = View.VISIBLE
        featuredCategoryContainerLayout?.removeAllViews()


        for (featuredCategory in list) {
            if (featuredCategory.products.isNullOrEmpty()) continue

            inflateAsync(R.layout.featured_list_layout, homePageRootView as RelativeLayout) {

                    linearLayout ->

                linearLayout.visibility = View.VISIBLE
                linearLayout.tvTitle.text = featuredCategory.name
                linearLayout.ivMore.visibility = if(featuredCategory.subCategories?.isNullOrEmpty() == true)
                    View.GONE else View.VISIBLE
                linearLayout.btnSeeAll?.text = DbHelper.getString("home.seeall")


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

                linearLayout.ivMore.setOnClickListener {
                    populateBottomSheet(featuredCategory.subCategories)
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

                featuredCategoryContainerLayout?.addView(linearLayout)
            }

        }
    }

    private fun populateFeaturedProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            featuredProductLayout?.visibility = View.GONE
            return
        }

        featuredProductLayout?.visibility = View.VISIBLE
        featuredProductLayout?.tvTitle?.text = DbHelper.getString("homepage.products")
        featuredProductLayout?.btnSeeAll?.visibility = View.GONE
        featuredProductLayout?.btnSeeAll?.text = DbHelper.getString("home.seeall")

        featuredProductLayout?.rvList?.adapter = FeaturedProductAdapter(list, productClickListener)

    }

    private fun populateBestSellingProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            bestSellingLayout.visibility = View.GONE
            return
        }

        bestSellingLayout.visibility = View.VISIBLE
        bestSellingLayout.tvTitle.text = DbHelper.getString("bestsellers")
        bestSellingLayout.btnSeeAll.visibility = View.GONE
        bestSellingLayout?.btnSeeAll?.text = DbHelper.getString("home.seeall")

        bestSellingLayout?.rvList?.adapter = FeaturedProductAdapter(list, productClickListener)
    }

    private fun populateManufacturerList(list: List<Manufacturer>) {
        if (list.isNullOrEmpty()) {
            featuredManufacturerLayout?.visibility = View.GONE
            return
        }

        featuredManufacturerLayout?.visibility = View.VISIBLE
        featuredManufacturerLayout?.divider?.visibility = View.INVISIBLE
        featuredManufacturerLayout?.tvTitle?.text = DbHelper.getString("manufacturers")
        featuredManufacturerLayout?.btnSeeAll?.visibility = View.GONE
        featuredManufacturerLayout?.btnSeeAll?.text = DbHelper.getString("home.seeall")

        val itemClickListener = object : ItemClickListener<Manufacturer> {
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
        }

        featuredManufacturerLayout?.rvList?.adapter =
            ManufacturerListAdapter(list, itemClickListener)
    }

    private fun populateBottomSheet(subCategories: List<SubCategory>) {
        bottomSheetLayout.subcategoryNameHolder.removeAllViews()

        for (item in subCategories) {
            val v:View = layoutInflater.inflate(R.layout.item_home_bs_options, homePageRootView as RelativeLayout, false)

            v.findViewById<TextView>(R.id.tvName).text = item.name

            v.setOnClickListener {
                bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                replaceFragmentSafely(ProductListFragment.newInstance(
                    item.name ?: "", item.id!!, ProductListFragment.GetBy.CATEGORY
                ))
            }

            bottomSheetLayout.subcategoryNameHolder.addView(v)
        }

        bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}