package com.bs.ecommerce.account.addresses

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
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.account.addresses.model.CustomerAddressModel
import com.bs.ecommerce.account.addresses.model.CustomerAddressModelImpl
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.catalog.common.AddressModel
import com.bs.ecommerce.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.address_form.view.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.*
import kotlinx.android.synthetic.main.custom_attribute_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_address_edit.*

class AddOrEditAddressFragment : BaseFragment() {

    private val formUtil: AddressFormUtil by lazy {
        AddressFormUtil(addressForm, requireContext())
    }

    private lateinit var countrySelectListener: ItemClickListener<AvailableCountry>
    private var customAttributeManager: CustomAttributeManager? = null
    private lateinit var bsBehavior: BottomSheetBehavior<*>

    private lateinit var model: CustomerAddressModel

    override fun getLayoutId(): Int = R.layout.fragment_address_edit

    override fun getRootLayout(): RelativeLayout? = editAddressRootLayout

    override fun createViewModel(): BaseViewModel =
        AddressViewModel()

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_CUSTOMER_ADDRESS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewCreated) {

            val editMode = arguments?.getBoolean(key_is_edit)
            val id = arguments?.getInt(key_id)

            initView()

            viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
            model =
                CustomerAddressModelImpl()

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

    private fun initView() {
        bsBehavior = BottomSheetBehavior.from(bottomSheetLayoutCart)

        tvDone.setOnClickListener{
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //dynamicAttributeView?.onBottomSheetClose()
        }

        btnSave?.text = DbHelper.getString(Const.SAVE_BUTTON)

        btnSave.setOnClickListener {
            val userAddress: AddressModel? = (viewModel as AddressViewModel).addressLD.value

            if (userAddress != null && formUtil.isFormDataValid(userAddress)) {

                (viewModel as AddressViewModel).apply {

                    // custom attribute fields value
                    val formValue = customAttributeManager
                        ?.getFormData(Api.addressAttributePrefix) ?: KeyValueFormData()

                    formUtil.validAddress?.let {
                        if (isEditMode) {
                            updateAddress(it, formValue.formValues, model)
                        } else {
                            saveCustomerAddress(it, formValue.formValues, model)
                        }
                    }
                }
            }
        }

        countrySelectListener = object : ItemClickListener<AvailableCountry> {
            override fun onClick(view: View, position: Int, data: AvailableCountry) {

                // load states for selected country
                if (data.value != "0") {
                    (viewModel as AddressViewModel).getStatesByCountryVM(
                        data.value, CheckoutModelImpl()
                    )
                }
            }
        }
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
                    toast(if (isEditMode) DbHelper.getString(Const.ADDRESS_UPDATED_SUCCESSFULLY) else DbHelper.getString(Const.ADDRESS_SAVED_SUCCESSFULLY))

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

        formUtil.apply {

            populateCountrySpinner(
                address.availableCountries ?: listOf(),
                countrySelectListener
            )

            prepareEditText(address)
        }

        // setup product attributes
        customAttributeManager =
            CustomAttributeManager(
                attributes = address.customAddressAttributes ?: listOf(),
                attributeViewHolder = addressForm.customAttributeViewHolder,
                attributeValueHolder = bottomSheetLayoutCart.attributeValueHolder,
                bsBehavior = bsBehavior
            )

        customAttributeManager?.attachAttributesToFragment()
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