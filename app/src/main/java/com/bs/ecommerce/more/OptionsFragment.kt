package com.bs.ecommerce.more

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.login.LoginFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.viewmodel.OptionViewModel
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.MyApplication
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.inflate
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_options.*
import kotlinx.android.synthetic.main.options_layout.view.*

class OptionsFragment : BaseFragment() {

    private var viewCreated = false
    private var rootView: View? = null


    override fun getFragmentTitle() = R.string.title_more

    override fun getLayoutId(): Int = R.layout.fragment_options

    override fun getRootLayout(): RelativeLayout? = optionsFragmentRootLayout

    override fun createViewModel(): BaseViewModel =
        OptionViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null)
            rootView = container?.inflate(R.layout.fragment_options)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!viewCreated) {

            (viewModel as OptionViewModel).optionsLD.observe(viewLifecycleOwner, Observer { options ->
                ll_option_holder.removeAllViews()

                for(i in options) {
                    val optionView = layoutInflater.inflate(R.layout.options_layout, null)
                    optionView.tag = i.nameResId

                    optionView.tvOptionName.text = getString(i.nameResId)
                    optionView.ivOptionIcon.setImageResource(i.iconResId)

                    optionView.setOnClickListener {

                        when(i.nameResId) {
                            R.string.settings ->
                                replaceFragmentSafely(SettingsFragment())

                            R.string.login ->
                                replaceFragmentSafely(LoginFragment())

                            R.string.log_out ->
                                logoutConfirmationDialog()

                            R.string.privacy_policy ->
                                replaceFragmentSafely(PrivacyPolicyFragment())

                            R.string.my_orders ->
                                replaceFragmentSafely(CustomerOrdersFragment())
                        }
                    }

                    ll_option_holder.addView(optionView)
                }
            })

            if(prefObject.getPrefsBoolValue(PrefSingleton.IS_LOGGED_IN))
                (viewModel as OptionViewModel).loadOptions(loggedIn = true)
            else
                (viewModel as OptionViewModel).loadOptions(loggedIn = false)



            viewCreated = true
        }
    }

    private fun logoutConfirmationDialog()
    {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage(R.string.are_you_sure_logout).setTitle(R.string.log_out)

        builder.setPositiveButton(R.string.yes) { _, _ -> performLogout() }
        builder.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout()
    {
        NetworkUtil.token = ""
        MyApplication.myCartCounter = 0
        prefObject.setPrefs(PrefSingleton.TOKEN_KEY, "")
        prefObject.setPrefs(PrefSingleton.IS_LOGGED_IN, false)
        //LoginManager.getInstance().logOut()
        requireActivity().invalidateOptionsMenu()

        (viewModel as OptionViewModel).changeLogInText()
    }
}