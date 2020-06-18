package com.bs.ecommerce.home.homepage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.base.ToolbarLogoBaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.FeaturedProductAdapter
import com.bs.ecommerce.home.homepage.model.HomePageModel
import com.bs.ecommerce.home.homepage.model.HomePageModelImpl
import com.bs.ecommerce.home.homepage.model.data.SliderData
import com.bs.ecommerce.home.manufaturer.ManufacturerListAdapter
import com.bs.ecommerce.home.manufaturer.ManufacturerListFragment
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.more.TopicFragment
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.featured_list_layout.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_fragment_bottomsheet.view.*
import kotlinx.android.synthetic.main.home_page_banner.view.*
import java.net.URL
import kotlin.concurrent.thread


class HomeFragment : ToolbarLogoBaseFragment() {

    private lateinit var model: HomePageModel
    private var observeLiveDataChange = true

    private lateinit var productClickListener: ItemClickListener<ProductSummary>

    private var bannerThread: Thread? = null


    override fun getFragmentTitle() =  DbHelper.getString(Const.HOME_NAV_HOME)

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getRootLayout(): RelativeLayout? = homePageRootView

    override fun createViewModel(): BaseViewModel = MainViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        if (!viewCreated) {
            "freeze_".showLog("view created")
            model = HomePageModelImpl(requireContext())

            (viewModel as MainViewModel).getAllLandingPageProducts(model)

            initComponents()
            observeLiveDataChange = true
        }
        else {
            "freeze_".showLog("view restored")
            observeLiveDataChange = false
        }

        setLiveDataListeners()
    }

    private fun callHomePageProducts()
    {
        (viewModel as MainViewModel).apply {
            observeLiveDataChange = true

            getAllLandingPageProducts(model)
        }
    }

    override fun onResume() {
        super.onResume()

        if((activity as MainActivity).notificationClicked)
        {
            callHomePageProducts()
            (activity as MainActivity).notificationClicked = false
        }
        "freeze_".showLog("onResume")
    }

    private fun initComponents() {
        "freeze_".showLog("initComponent - start")

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

            swipeRefreshLayout.isRefreshing = false
            bannerThread = null

            callHomePageProducts()
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
        "freeze_".showLog("initComponent - end")
    }

    private fun setLiveDataListeners() {
        "freeze_".showLog("setLiveData - start")

        with(viewModel as MainViewModel)
        {
            appSettingsLD.observe(viewLifecycleOwner, Observer { appLanding ->

                if(observeLiveDataChange && !MyApplication.navigateFromCheckoutCompleteToHomePage)
                    appLanding.peekContent()?.totalShoppingCartProducts?.let { updateTopCart(it) }

                MyApplication.navigateFromCheckoutCompleteToHomePage = false
            })

            featuredProductListLD.observe(viewLifecycleOwner,
                Observer { list ->
                    if(observeLiveDataChange) populateFeaturedProductList(list)
                })

            manufacturerListLD.observe(viewLifecycleOwner, Observer { list ->
                if(observeLiveDataChange) populateManufacturerList(list)
            })

            featuredCategoryLD.observe(viewLifecycleOwner, Observer { list ->
                try {
                    if (observeLiveDataChange) populateFeaturedCategoryList(list)
                } catch (e: IllegalStateException) {
                    "crash".showLog("asyncInflation - $e")
                }
            })


            bestSellingProductLD.observe(viewLifecycleOwner, Observer { list ->
                if(observeLiveDataChange) populateBestSellingProductList(list)
            })


            imageBannerLD.observe(viewLifecycleOwner, Observer { sliderData ->
                try {
                    if(observeLiveDataChange) populateBanner(sliderData)
                } catch (e: IllegalStateException) {
                    "banner_crash".showLog("$e")
                }
            })

            homePageLoader.observe(viewLifecycleOwner, Observer { isShowLoader -> showHideLoader(isShowLoader) })

            addedToWishListLD.observe(viewLifecycleOwner, Observer { action ->

                action?.getContentIfNotHandled()?.let { product ->
                    replaceFragmentSafely(ProductDetailFragment.newInstance(
                        product.id?.toLong() ?: -1L, product.name))
                }

                blockingLoader.hideDialog()
            })
        }

        "freeze_".showLog("setLiveData - end")
    }

    private fun populateBanner(sliderData: SliderData) {
        "freeze_".showLog("Populating banner")

        if (sliderData.isEnabled == false || sliderData.sliders.isNullOrEmpty()) {
            banner?.visibility = View.GONE
            return
        }

        if(bannerThread!=null || bannerThread?.isAlive == true) return

        banner?.visibility = View.VISIBLE

        banner?.view_pager_slider1?.apply {
            removeAllSliders()
            setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            view?.findViewById<View>(R.id.circle_indicator)?.let {
                setCustomIndicator(it as PagerIndicator)
            }
        }

        var biggestImageAR = 100000F

        bannerThread = thread {
            for ((i, sliderModel) in sliderData.sliders.withIndex()) {

                if(!isAdded) return@thread
                "freeze_".showLog("Populating banner $i")

                requireActivity().runOnUiThread {
                    val textSliderView = DefaultSliderView(requireContext())

                    textSliderView.image(sliderModel.imageUrl).scaleType =
                        BaseSliderView.ScaleType.CenterInside

                    textSliderView.setOnSliderClickListener {

                        sliderModel.id?.let {

                            when(sliderModel.sliderType) {

                                SliderType.PRODUCT ->
                                    replaceFragmentSafely(ProductDetailFragment.newInstance(it.toLong(), ""))

                                SliderType.CATEGORY ->
                                    replaceFragmentSafely(ProductListFragment.newInstance("", it, ProductListFragment.GetBy.CATEGORY))

                                SliderType.MANUFACTURER ->
                                    replaceFragmentSafely(ProductListFragment.newInstance("", it, ProductListFragment.GetBy.MANUFACTURER))

                                SliderType.TOPIC ->
                                    replaceFragmentSafely(TopicFragment.newInstance(it))

                                SliderType.VENDOR ->
                                    replaceFragmentSafely(ProductListFragment.newInstance("", it, ProductListFragment.GetBy.VENDOR))
                            }
                        }
                    }

                    banner?.view_pager_slider1?.addSlider(textSliderView)
                }

                //Execute all the long running tasks here
                val url = URL(sliderModel.imageUrl)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                val bitmapWidth: Float = bitmap?.width?.toFloat() ?: 1f
                val bitmapHeight: Float = bitmap?.height?.toFloat() ?: 1f

                val thisImageAR = (bitmapWidth / bitmapHeight)

                if (thisImageAR < biggestImageAR)
                    biggestImageAR = thisImageAR

                "banner_ar".showLog("ar of image $i: $thisImageAR, largest ar: $biggestImageAR")
            }

            banner?.aspectRatioView?.setmAspectRatio(biggestImageAR)
        }

    }

    private fun populateFeaturedCategoryList(list: List<CategoryModel>) {
        "freeze_".showLog("Populating featuredCat")

        if (list.isNullOrEmpty()) {
            featuredCategoryContainerLayout?.visibility = View.GONE
            return
        }

        featuredCategoryContainerLayout?.visibility = View.VISIBLE
        featuredCategoryContainerLayout?.removeAllViews()


        for ((j, featuredCategory) in list.withIndex()) {
            if (featuredCategory.products.isNullOrEmpty()) continue

            inflateAsync(R.layout.featured_list_layout, homePageRootView as RelativeLayout) {

                    linearLayout ->

                if(!isAdded) return@inflateAsync
                "freeze_".showLog("Populating featuredCat $j")

                linearLayout.visibility = View.VISIBLE
                linearLayout.tvTitle.text = featuredCategory.name
                linearLayout.ivMore.visibility = if(featuredCategory.subCategories?.isNullOrEmpty() == true)
                    View.GONE else View.VISIBLE
                linearLayout.btnSeeAll?.text = DbHelper.getString(Const.COMMON_SEE_ALL)


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

                    context?.let {
                        setHasFixedSize(true)
                        addItemDecoration(RecyclerViewMargin(15, 1, false))
                        layoutManager = LinearLayoutManager(
                            it, LinearLayoutManager.HORIZONTAL, false
                        )

                        adapter = FeaturedProductAdapter(
                            featuredCategory.products, productClickListener
                        )
                    }
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
        "freeze_".showLog("Populating featured products")

        featuredProductLayout?.visibility = View.VISIBLE
        featuredProductLayout?.tvTitle?.text = DbHelper.getString(Const.HOME_FEATURED_PRODUCT)
        featuredProductLayout?.btnSeeAll?.visibility = View.GONE
        featuredProductLayout?.btnSeeAll?.text = DbHelper.getString(Const.COMMON_SEE_ALL)

        featuredProductLayout?.rvList?.adapter = FeaturedProductAdapter(list, productClickListener)

    }

    private fun populateBestSellingProductList(list: List<ProductSummary>) {
        if (list.isNullOrEmpty()) {
            bestSellingLayout.visibility = View.GONE
            return
        }

        bestSellingLayout?.visibility = View.VISIBLE
        bestSellingLayout?.tvTitle?.text = DbHelper.getString(Const.HOME_BESTSELLER)
        bestSellingLayout?.btnSeeAll?.visibility = View.GONE
        bestSellingLayout?.btnSeeAll?.text = DbHelper.getString(Const.COMMON_SEE_ALL)

        bestSellingLayout?.rvList?.adapter = FeaturedProductAdapter(list, productClickListener)
    }

    private fun populateManufacturerList(list: List<Manufacturer>) {
        if (list.isNullOrEmpty()) {
            featuredManufacturerLayout?.visibility = View.GONE
            return
        }
        "freeze_".showLog("Populating manufacturer list")

        featuredManufacturerLayout?.visibility = View.VISIBLE
        featuredManufacturerLayout?.divider?.visibility = View.INVISIBLE
        featuredManufacturerLayout?.tvTitle?.text = DbHelper.getString(Const.HOME_MANUFACTURER)
        featuredManufacturerLayout?.btnSeeAll?.text = DbHelper.getString(Const.COMMON_SEE_ALL)

        featuredManufacturerLayout?.btnSeeAll?.setOnClickListener {
            replaceFragmentSafely(ManufacturerListFragment())
        }

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
            ManufacturerListAdapter(
                list,
                itemClickListener
            )
    }

    private fun populateBottomSheet(subCategories: List<SubCategory>) {
        "freeze_".showLog("Populating bottomSheet")

        val bsDialog = BottomSheetDialog(requireContext(), R.style.BsDialog)

        val dialogView: LinearLayout = layoutInflater.inflate(
            R.layout.home_fragment_bottomsheet, getRootView() as ViewGroup, false
        ) as LinearLayout

        dialogView.tvCloseBs.text = DbHelper.getString(Const.COMMON_DONE)
        dialogView.tvCloseBs.setOnClickListener {
            bsDialog.hide()
        }

        for (item in subCategories) {
            val v:View = layoutInflater.inflate(R.layout.item_home_bs_options, homePageRootView as RelativeLayout, false)

            v.findViewById<TextView>(R.id.tvName).text = item.name

            v.setOnClickListener {
                bsDialog.hide()

                replaceFragmentSafely(ProductListFragment.newInstance(
                    item.name ?: "", item.id ?: -1, ProductListFragment.GetBy.CATEGORY
                ))
            }

            dialogView.subcategoryNameHolder.addView(v)
        }

        bsDialog.setContentView(dialogView)
        bsDialog.show()
    }
}