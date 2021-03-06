package com.bs.ecommerce.account.rewardpoint

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
import com.bs.ecommerce.account.rewardpoint.model.RewardPointModel
import com.bs.ecommerce.account.rewardpoint.model.RewardPointModelImpl
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.rewardpoint.model.data.RewardPoint
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.TextUtils
import kotlinx.android.synthetic.main.fragment_reward_point.*
import kotlinx.android.synthetic.main.item_reward_points.view.*
import java.lang.ref.WeakReference

class RewardPointFragment : BaseFragment() {

    private lateinit var model: RewardPointModel

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_REWARD_POINT)

    override fun getLayoutId(): Int = R.layout.fragment_reward_point

    override fun getRootLayout(): RelativeLayout? = productReviewRootLayout

    override fun createViewModel(): BaseViewModel =
        RewardPointViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            model =
                RewardPointModelImpl()
            viewModel = ViewModelProvider(this).get(RewardPointViewModel::class.java)

            (viewModel as RewardPointViewModel).getRewardPoints(model)

            setupView()
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as RewardPointViewModel).apply {

            rewardPointLD.observe(viewLifecycleOwner, Observer { rewardPoint ->

                val text1 = DbHelper.getString(Const.REWARD_POINT_BALANCE_CURRENT)
                    .replace("{0}", rewardPoint.rewardPointsBalance.toString())
                    .replace("({1})", rewardPoint.rewardPointsAmount.toString())

                val text2 = DbHelper.getString(Const.REWARD_POINT_BALANCE_MIN)
                    .replace("{0}", rewardPoint.minimumRewardPointsBalance.toString())
                    .replace("({1})", rewardPoint.minimumRewardPointsAmount.toString())

                tvRewardBalance.text = text1.plus("\n").plus(text2)
                rewardBalanceCardView.visibility = View.VISIBLE


                if (rewardPoint?.rewardPoints?.isNullOrEmpty() == true) {
                    tvNoData.visibility = View.VISIBLE
                } else {
                    tvNoData.visibility = View.GONE

                    if (rewardPoint?.rewardPoints?.isNotEmpty() == true)
                        (rvReview.adapter as ReviewAdapter).addData(rewardPoint.rewardPoints)
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
        tvNoData.text = DbHelper.getString(Const.REWARD_NO_HISTORY)

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

    inner class ReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var list: MutableList<RewardPoint> = mutableListOf()
        private val textUtils = TextUtils()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_reward_points, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = list[position]

            holder.itemView.apply {
                tvRpDate.text = DbHelper.getString(Const.REWARD_POINT_DATE)
                tvRpEndDate.text = DbHelper.getString(Const.REWARD_POINT_END_DATE)
                tvRpMsg.text = DbHelper.getString(Const.REWARD_POINT_MSG)
                tvRpPoint.text = DbHelper.getString(Const.REWARD_POINT_)
                tvRpPointBlns.text = DbHelper.getString(Const.REWARD_POINT_BALANCE)

                tvPoints?.text = item.points?.toString()
                tvBalance?.text = item.pointsBalance
                tvMessage?.text = item.message

                tvCreatedOn?.text = textUtils.tzTimeConverter(
                    item.createdOn, WeakReference(holder.itemView.context)
                )

                tvEndDate?.text = textUtils.tzTimeConverter(
                    item.endDate, WeakReference(holder.itemView.context)
                )
            }
        }

        fun addData(newData: List<RewardPoint>) {
            list.clear()
            list.addAll(newData)
            notifyDataSetChanged()
        }

    }

    companion object {
        @JvmStatic
        private val PRODUCT_ID = "productId"

        @JvmStatic
        fun newInstance(productId: Long): RewardPointFragment {

            return RewardPointFragment().apply {
                val args = Bundle()
                args.putLong(PRODUCT_ID, productId)
                arguments = args
            }
        }
    }
}