package com.bs.ecommerce.account.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.ecommerce.R
import com.bs.ecommerce.account.orders.model.ReturnReqModel
import com.bs.ecommerce.account.orders.model.ReturnReqModelImpl
import com.bs.ecommerce.account.orders.model.data.AvailableReturnAction
import com.bs.ecommerce.account.orders.model.data.AvailableReturnReason
import com.bs.ecommerce.account.orders.model.data.ReturnReqFormData
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.RecyclerViewMargin
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_return_request.*
import java.io.File


class ReturnRequestFragment: BaseFragment() {

    private lateinit var model: ReturnReqModel

    override fun getFragmentTitle() = DbHelper.getString(Const.ORDER_RETURN_ITEMS)

    override fun getLayoutId(): Int = R.layout.fragment_return_request

    override fun getRootLayout(): RelativeLayout? = returnRequestRoot

    override fun createViewModel(): BaseViewModel = ReturnRequestViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {
            val orderId = arguments?.getInt(ORDER_ID)

            orderId?.let {
                model = ReturnReqModelImpl()
                viewModel = ViewModelProvider(this).get(ReturnRequestViewModel::class.java)

                (viewModel as ReturnRequestViewModel).getFormData(orderId, model)
            }
        }

        setLiveDataObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            data?.data?.path?.let {
                val src: String = data.data?.path ?: ""

                val source = File(src)

                if (source.isFile) {
                    (viewModel as ReturnRequestViewModel)
                        .uploadFile(source, model)

                    toast("File selection success")
                } else {
                    toast("File selection failed")
                }

            }
        } else {
            toast("File selection failed")
        }
    }

    private fun setLiveDataObserver() {

        (viewModel as ReturnRequestViewModel).apply {

            returnReqLD.observe(viewLifecycleOwner, Observer { data->
                initView(data)
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { showHideLoader(it) })
        }
    }

    private fun initView(data: ReturnReqFormData) {

        llForm?.visibility = View.VISIBLE
        btnSubmit?.visibility = View.VISIBLE

        tvUploadAction?.text = DbHelper.getString(Const.RETURN_REQ_UPLOAD)
        tvWhichProduct?.text = DbHelper.getString(Const.RETURN_REQ_TITLE_WHICH_ITEM)
        tvWhy?.text = DbHelper.getString(Const.RETURN_REQ_TITLE_WHY)
        btnUpload?.text = DbHelper.getString(Const.RETURN_REQ_UPLOAD_FILE)
        btnSubmit?.text = DbHelper.getString(Const.RETURN_REQ_SUBMIT)
        etComments?.hint = DbHelper.getString(Const.RETURN_REQ_COMMENTS)

        btnUpload?.setOnClickListener {
            // TODO FILE UPLOADER

            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, 123)
        }

        btnSubmit?.setOnClickListener {
            // TODO SUBMIT
        }

        populateSpinner(data)

        rvProdList?.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecyclerViewMargin(15, 1, true))
            adapter = ReturnReqProductAdapter(data.items ?: listOf(), null)
        }
    }

    private fun populateSpinner(data: ReturnReqFormData) {

        val reasonsArray = data.availableReturnReasons ?: listOf()
        (reasonsArray as MutableList).add(0, AvailableReturnReason(null, -1, DbHelper.getString(Const.RETURN_REQ_REASON)))

        val reasonAdapter: ArrayAdapter<AvailableReturnReason> =
            ArrayAdapter<AvailableReturnReason>(
                requireContext(),
                R.layout.simple_spinner_item,
                reasonsArray
            )

        reasonAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spReason?.adapter = reasonAdapter

        val actionArray = data.availableReturnActions ?: listOf()
        (actionArray as MutableList).add(0, AvailableReturnAction(null, -1, DbHelper.getString(Const.RETURN_REQ_ACTION)))

        val actionAdapter: ArrayAdapter<AvailableReturnAction> =
            ArrayAdapter<AvailableReturnAction>(
                requireContext(),
                R.layout.simple_spinner_item,
                actionArray
            )

        actionAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spAction?.adapter = actionAdapter
    }

    companion object {
        @JvmStatic
        private val ORDER_ID = "orderId"

        @JvmStatic
        fun newInstance(orderId: Int): ReturnRequestFragment {
            val fragment = ReturnRequestFragment()
            val args = Bundle()
            args.putInt(ORDER_ID, orderId)
            fragment.arguments = args
            return fragment
        }
    }
}