package com.bs.ecommerce.account.addresses

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.addresses.model.CustomerAddressModel
import com.bs.ecommerce.account.addresses.model.CustomerAddressModelImpl
import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.replaceFragmentSafely
import kotlinx.android.synthetic.main.fragment_customer_address.*

class CustomerAddressFragment : BaseFragment() {

    private lateinit var model: CustomerAddressModel
    private lateinit var listAdapter: AddressListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_customer_address

    override fun getRootLayout(): RelativeLayout? = addressRootLayout

    override fun createViewModel(): BaseViewModel =
        AddressViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_CUSTOMER_ADDRESS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {
            model =
                CustomerAddressModelImpl()
            viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)

            setupView()

            (viewModel as AddressViewModel).getCustomerAddresses(model)
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {
        (viewModel as AddressViewModel).apply {

            addressListLD.observe(viewLifecycleOwner, Observer { data ->

                tvNoData.visibility = if (data?.addresses.isNullOrEmpty())
                    View.VISIBLE else View.GONE

                listAdapter.addData(data?.addresses)
            })


            addressDeleteLD.observe(viewLifecycleOwner, Observer { position ->
                rvAddress?.adapter?.let {
                    if (position >= 0) {
                        (it as AddressListAdapter).removeData(position)

                        tvNoData.visibility = if (it?.itemCount == 0)
                            View.VISIBLE else View.GONE
                    }
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


        btnAddNew?.text = DbHelper.getString(Const.ADD_NEW_ADDRESS)

        val clickListener = object : ItemClickListener<AddressModel> {
            override fun onClick(view: View, position: Int, data: AddressModel) {

                when (view.id) {
                    R.id.icEdit ->
                        replaceFragmentSafely(AddOrEditAddressFragment.newInstance(data, true))

                    R.id.icDelete ->
                        showDeleteConfirmationDialog(data, position)
                }
            }
        }

        rvAddress?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(10, 1, true))

            listAdapter = AddressListAdapter(
                clickListener
            )
            adapter = listAdapter
        }

        btnAddNew.setOnClickListener {
            replaceFragmentSafely(AddOrEditAddressFragment.newInstance(null, false))
        }


    }

    // FIXME Bad Practice. Inter fragment communication should be done with interface. Replace ASAP
    fun updateAddressList() {
        (viewModel as AddressViewModel).getCustomerAddresses(model)
    }

    private fun showDeleteConfirmationDialog(data: AddressModel, position: Int) {

        AlertDialog.Builder(requireActivity()).apply {

            setMessage(DbHelper.getString(Const.CONFIRM_DELETE_ADDRESS)).setTitle(DbHelper.getString(Const.DELETE_ADDRESS))

            setPositiveButton(DbHelper.getString(Const.COMMON_YES)) { _, _ ->
                (viewModel as AddressViewModel).deleteAddress(data, position, model)
            }

            setNegativeButton(DbHelper.getString(Const.COMMON_NO)) { dialog, _ ->
                dialog.dismiss()
            }

            create()

        }.show()
    }
}