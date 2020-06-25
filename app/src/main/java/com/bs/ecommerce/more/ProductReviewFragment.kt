package com.bs.ecommerce.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.more.model.ReviewModel
import com.bs.ecommerce.more.model.ReviewModelImpl
import com.bs.ecommerce.more.viewmodel.ReviewViewModel
import com.bs.ecommerce.product.AddReviewFragment
import com.bs.ecommerce.product.model.data.Helpfulness
import com.bs.ecommerce.product.model.data.ProductReviewItem
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.fragment_product_review.*
import kotlinx.android.synthetic.main.item_product_review.view.*

class ProductReviewFragment : BaseFragment() {

    private lateinit var model: ReviewModel

    override fun getFragmentTitle() = DbHelper.getString(Const.TITLE_REVIEW)

    override fun getLayoutId(): Int = R.layout.fragment_product_review

    override fun getRootLayout(): RelativeLayout? = productReviewRootLayout

    override fun createViewModel(): BaseViewModel = ReviewViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            val productId: Long = arguments?.getLong(PRODUCT_ID) ?: 0

            model = ReviewModelImpl()
            viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

            (viewModel as ReviewViewModel).getProductReview(productId, model)

            setupView()
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as ReviewViewModel).apply {

            productReviewLD.observe(viewLifecycleOwner, Observer { data ->

                if (data?.items?.isNullOrEmpty() == true) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE

                    if (data?.items?.isNotEmpty() == true)
                        (rvReview.adapter as ReviewAdapter).addData(data.items)
                }

                val reviewEnabled = data?.addProductReview?.canCurrentCustomerLeaveReview == true

                btnAddReview.visibility = if(reviewEnabled) View.VISIBLE else View.GONE
            })

            operationalReviewIdLD.observe(viewLifecycleOwner, Observer { data ->

                data?.let {
                    (rvReview.adapter as ReviewAdapter).updateReviewHelpfulnessCount(it)
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    private fun setupView() {

        tvNoData.text = DbHelper.getString(Const.COMMON_NO_DATA)

        btnAddReview.text = DbHelper.getString(Const.REVIEW_WRITE)
        btnAddReview.setOnClickListener {
            AddReviewFragment().show(
                childFragmentManager, AddReviewFragment::class.java.simpleName
            )
        }

        val mLayoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        rvReview?.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            addItemDecoration(RecyclerViewMargin(10, 1, true))
            adapter = ReviewAdapter()
        }
    }

    fun postProductReview(title: String, text: String, rating: Int) {

        with(viewModel as ReviewViewModel) {

            productReviewLD.value?.addProductReview?.let {

                val postData = productReviewLD.value!!

                postData.addProductReview?.let { review ->
                    review.title = title
                    review.reviewText = text
                    review.rating = rating
                }

                postProductReview(postData, model)
            }
        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: MutableList<ProductReviewItem> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_review, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = list[position]

            holder.itemView.apply {

                ivReviewAvatar?.loadImg(item.customerAvatarUrl, R.drawable.ic_vendor)
                tvReviewTitle.text = item.title
                tvReviewText.text = item.reviewText
                tvDate.text = item.writtenOnStr
                tvReviewBy.text = item.customerName
                tvHelp.text = DbHelper.getString(Const.REVIEW_HELPFUL)
                tvYes.text = DbHelper.getString(Const.COMMON_YES)
                tvNo.text = DbHelper.getString(Const.COMMON_NO)

                tvHelpfulnessCount.text = item.helpfulness?.let {
                    "(${it.helpfulYesTotal ?: 0}/${it.helpfulNoTotal ?: 0})"
                }

                ratingBar.rating = (item.rating ?: 0).toFloat()

                tvYes.setOnClickListener {
                    (viewModel as ReviewViewModel).postReviewHelpfulness(
                        item.helpfulness?.productReviewId!!,
                        positive = true,
                        model = model
                    )
                }

                tvNo.setOnClickListener {
                    (viewModel as ReviewViewModel).postReviewHelpfulness(
                        item.helpfulness?.productReviewId!!,
                        positive = false,
                        model = model
                    )
                }
            }
        }

        fun addData(newData: List<ProductReviewItem>) {
            list.clear()
            list.addAll(newData)
            notifyDataSetChanged()
        }

        fun updateReviewHelpfulnessCount(data: Helpfulness) {
            val index = list.indexOfFirst { reviewItem -> reviewItem.id == data.productReviewId }

            if(index > -1) {
                list[index].helpfulness = data

                notifyItemChanged(index)
            }
        }

    }

    companion object {
        @JvmStatic
        private val PRODUCT_ID = "productId"

        @JvmStatic
        fun newInstance(productId: Long): ProductReviewFragment {

            return ProductReviewFragment().apply {
                val args = Bundle()
                args.putLong(PRODUCT_ID, productId)
                arguments = args
            }
        }
    }
}