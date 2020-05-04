package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.model.CheckoutModelImpl
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.more.model.CustomerAddressModel
import com.bs.ecommerce.more.model.CustomerAddressModelImpl
import com.bs.ecommerce.more.viewmodel.AddressViewModel
import com.bs.ecommerce.product.model.data.AddressModel
import com.bs.ecommerce.utils.AddressFormUtil
import com.bs.ecommerce.utils.ItemClickListener
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_address_edit.*

class AddOrEditAddressFragment : BaseFragment() {

    private val formUtil: AddressFormUtil by lazy {
        AddressFormUtil(addressForm, requireContext())
    }

    private lateinit var model: CustomerAddressModel

    override fun getLayoutId(): Int = R.layout.fragment_address_edit

    override fun getRootLayout(): RelativeLayout? = editAddressRootLayout

    override fun createViewModel(): BaseViewModel = AddressViewModel()

    override fun getFragmentTitle(): Int = R.string.title_addresses

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            val editMode = arguments?.getBoolean(key_is_edit)
            val id = arguments?.getInt(key_id)

            viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
            model = CustomerAddressModelImpl()

            (viewModel as AddressViewModel).apply {
                isEditMode = editMode == true

                if (isEditMode && id != null)
                    getExistingAddressFormData(id, model)
                else if (!isEditMode)
                    getNewAddressFormData(model)
            }
        }

        setLiveDataObserver()
    }

    private fun setLiveDataObserver() {

        (viewModel as AddressViewModel).apply {

            addressLD.observe(viewLifecycleOwner, Observer { address ->
                if (address != null)
                    setupView(address)
            })

            stateListLD.observe(viewLifecycleOwner, Observer { statesList ->
                formUtil.populateStateSpinner(addressLD.value?.stateProvinceId ?: 0, statesList)
            })


            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

            resetFormLD.observe(viewLifecycleOwner, Observer { resetForm ->

                if (resetForm && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    toast(if (isEditMode) R.string.address_update_succss else R.string.address_saved)

                    // Update address list to show changes
                    // FIXME Bad Practice. Inter fragment communication should be done with interface. Replace ASAP
                    val fragment =
                        requireActivity().supportFragmentManager.findFragmentByTag(
                            CustomerAddressFragment::class.java.simpleName
                        )

                    if(fragment!=null && fragment is CustomerAddressFragment)
                        fragment.updateAddressList()

                    // close this fragment
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })
        }
    }

    private fun setupView(address: AddressModel) {

        formParentLL.visibility = View.VISIBLE

        val countrySelectListener = object : ItemClickListener<AvailableCountry> {
            override fun onClick(view: View, position: Int, data: AvailableCountry) {

                // load states for selected country
                if (data.value != "0") {
                    (viewModel as AddressViewModel).getStatesByCountryVM(
                        data.value, CheckoutModelImpl(requireContext())
                    )
                }
            }
        }

        formUtil.apply {

            populateCountrySpinner(
                address.availableCountries ?: listOf(),
                countrySelectListener
            )

            prepareEditText(address)
        }

        btnSave.setOnClickListener {
            val userAddress: AddressModel? = (viewModel as AddressViewModel).addressLD.value

            if (userAddress != null && formUtil.isFormDataValid(userAddress)) {

                (viewModel as AddressViewModel).apply {

                    if (isEditMode) {
                        updateAddress(formUtil.validAddress!!, model)
                    } else {
                        saveCustomerAddress(formUtil.validAddress!!, model)
                    }
                }
            }
        }
    }

    companion object {

        private const val key_is_edit: String = "key_is_edit"
        private const val key_id: String = "key_id"

        fun newInstance(address: AddressModel?, isEdit: Boolean): AddOrEditAddressFragment {

            val bundle = Bundle().apply {
                putBoolean(key_is_edit, isEdit)

                if (isEdit) putInt(key_id, address?.id ?: -1)
            }

            return AddOrEditAddressFragment().apply {
                arguments = bundle
            }
        }
    }

}