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
import com.bs.ecommerce.cart.GiftCardAdapter
import com.bs.ecommerce.more.model.OrderModel
import com.bs.ecommerce.more.model.OrderModelImpl
import com.bs.ecommerce.more.viewmodel.OrderViewModel
import com.bs.ecommerce.product.model.data.Item
import com.bs.ecommerce.product.model.data.OrderDetailsData
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.confirm_order_card.view.*
import kotlinx.android.synthetic.main.fragment_customer_order_detail.*
import kotlinx.android.synthetic.main.item_order_details.view.*
import kotlinx.android.synthetic.main.table_order_total.*
import java.lang.ref.WeakReference

class OrderDetailsFragment : BaseFragment() {
    private lateinit var model: OrderModel

    override fun getFragmentTitle() = R.string.title_order_details

    override fun getLayoutId(): Int = R.layout.fragment_customer_order_detail

    override fun getRootLayout(): RelativeLayout? = customerOrderRootLayout

    override fun createViewModel(): BaseViewModel = OrderViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            val orderId = arguments?.getInt(ORDER_ID)

            if (orderId == null) {
                toast(R.string.invalid_id)

                requireActivity().supportFragmentManager.popBackStackImmediate()
                return
            }

            model = OrderModelImpl()
            viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

            (viewModel as OrderViewModel).getOrderDetails(orderId, model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as OrderViewModel).orderDetailsLD.observe(viewLifecycleOwner, Observer { data->
            initView(data)
        })

        (viewModel as OrderViewModel).isLoadingLD.observe(
            viewLifecycleOwner,
            Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
    }

    private fun initView(data: OrderDetailsData) {
        val orderDetails = getString(R.string.order_date)
            .plus(
                TextUtils().tzTimeConverter(
                    data.createdOn,
                    WeakReference(requireContext())
                )
            ).plus("\n").plus(getString(R.string.order_status)).plus(data.orderStatus).plus("\n")
            .plus(
                getString(R.string.order_total)
            ).plus(data.orderTotal)

        orderDetailsCard.tvCardTitle.text = getString(R.string.order_details)
        orderDetailsCard.tvCardDetails.text = getString(R.string.order_number).plus(data.customOrderNumber)
        orderDetailsCard.tvCardDetails2.text = orderDetails

        shippingAddressCard.tvCardTitle.text = getString(R.string.shipping_address)
        shippingAddressCard.tvCardDetails.text = data.shippingAddress?.firstName.plus(" ").plus(data.shippingAddress?.lastName)
        shippingAddressCard.tvCardDetails2.text = TextUtils().getFormattedAddress(
            data.shippingAddress, WeakReference(requireContext())
        )

        billingAddressCard.tvCardTitle.text = getString(R.string.billing_address)
        billingAddressCard.tvCardDetails.text = data.billingAddress?.firstName.plus(" ").plus(data.billingAddress?.lastName)
        billingAddressCard.tvCardDetails2.text = TextUtils().getFormattedAddress(
            data.billingAddress, WeakReference(requireContext())
        )

        shippingMethodCard.tvCardTitle.text = getString(R.string.shipping_method)
        shippingMethodCard.tvCardDetails.text = data.shippingMethod
        shippingMethodCard.tvCardDetails2.text = data.shippingStatus
        shippingMethodCard.ivCardThumb.visibility = View.VISIBLE

        paymentMethodCard.tvCardTitle.text = getString(R.string.payment_method)
        paymentMethodCard.tvCardDetails.text = data.paymentMethod
        paymentMethodCard.tvCardDetails2.text = data.paymentMethodStatus
        paymentMethodCard.ivCardThumb.visibility = View.VISIBLE


        checkoutProductList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = OrderItemsAdapter(data.items ?: listOf())
        }

        populateOrderTotal(data)

        orderDetailsScrollView.visibility = View.VISIBLE
    }

    private fun populateOrderTotal(data: OrderDetailsData) {
        with(data) {
            
            tvSubTotal?.text = orderSubtotal
            tvShippingCharge?.text = orderShipping


            if (displayTax==true && tax != null) {
                if (displayTaxRates == true)
                    taxRates?.get(0)?.rate?.let {
                        taxKey?.text = getString(R.string.tax).plus(it).plus("%s")
                    }

                tvTax?.text = tax
            } else
                taxLayout?.visibility = View.GONE

            tvTotal?.text = orderTotal

            if (orderTotalDiscount != null) {
                discountLayout?.visibility = View.VISIBLE
                tvDiscount?.text = orderTotalDiscount
                underDiscountDivider?.visibility = View.VISIBLE
            } else
                discountLayout?.visibility = View.GONE

            if (giftCards != null && giftCards.isNotEmpty()) {
                giftCardLayout?.visibility = View.VISIBLE

                underGiftCardDivider?.visibility = View.VISIBLE

                val giftCardAdapter = GiftCardAdapter(requireContext(), giftCards)
                giftCardRecyclerList?.layoutManager =
                    LinearLayoutManager(activity)
                giftCardRecyclerList?.adapter = giftCardAdapter
            } else
                giftCardLayout?.visibility = View.GONE

            if (orderTotal.isNullOrEmpty()) {
                tvTotal?.setText(R.string.calculated_during_checkout)
                tvTotal?.setTextColor(android.graphics.Color.RED)
            }

            if (orderShipping.isNullOrEmpty()) {
                tvTotal?.setText(R.string.calculated_during_checkout)
                tvTotal?.setTextColor(android.graphics.Color.RED)
            }
        }
    }

    //--------------------

    inner class OrderItemsAdapter(
        private val list: List<Item>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_order_details, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val item = list[position]

            holder.itemView.tvProductName.text = item.productName

            holder.itemView.tv1.text =
                getString(R.string.price).plus(": ").plus(item.unitPrice).plus("  ")
                    .plus(getString(R.string.quantity)).plus(": ").plus(item.quantity).plus("\n")
                    .plus(getString(R.string.total)).plus(": ").plus(item.subTotal)
        }
    }

    companion object {
        @JvmStatic
        private val ORDER_ID = "orderId"

        @JvmStatic
        fun newInstance(orderId: Int): OrderDetailsFragment {
            val fragment = OrderDetailsFragment()
            val args = Bundle()
            args.putInt(ORDER_ID, orderId)
            fragment.arguments = args
            return fragment
        }
    }
}