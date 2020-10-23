package com.bs.ecommerce.account.auth.customerInfo


import android.os.Bundle
import android.view.View
import com.bs.ecommerce.R
import com.bs.ecommerce.account.auth.register.RegisterFragment
import com.bs.ecommerce.account.auth.register.RegistrationViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.fragment_registration.*


class CustomerInfoFragment : RegisterFragment() {
    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_INFO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveBtn.text = DbHelper.getString(Const.SAVE_BUTTON)

        if (!viewCreated) {
            (viewModel as RegistrationViewModel).getCustomerInfoVM(model)
        }

    }

    override fun performSubmit() {
        // custom attribute fields value
        if(isValidInfo) {

            val formValue = customAttributeManager
                ?.getFormData(Api.customerAttributePrefix) ?: return

            customerInfo.apply {
                formValues = formValue.formValues
                data.availableCountries = listOf()
                data.availableStates = listOf()
                data.availableTimeZones = listOf()
            }

            (viewModel as RegistrationViewModel).apply {
                customerInfoUpdate = true
                postCustomerInfo(customerInfo, model)
            }
        }
    }

}
