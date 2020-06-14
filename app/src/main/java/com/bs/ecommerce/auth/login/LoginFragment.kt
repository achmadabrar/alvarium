package com.bs.ecommerce.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.AuthModel
import com.bs.ecommerce.auth.AuthModelImpl
import com.bs.ecommerce.auth.forgotpass.ForgotPasswordFragment
import com.bs.ecommerce.auth.login.data.FacebookLogin
import com.bs.ecommerce.auth.login.data.LoginData
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.register.RegisterFragment
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.utils.*
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException


class LoginFragment : BaseFragment()
{
    private lateinit var mainViewModel: MainViewModel
    private var callbackManager: CallbackManager? = null

    private lateinit var model: AuthModel

    override fun getFragmentTitle() = DbHelper.getString(Const.ACCOUNT_LOGIN)

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun getRootLayout(): RelativeLayout? = login_root_layout

    override fun createViewModel(): BaseViewModel = LoginViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = AuthModelImpl()

        viewModel  = ViewModelProvider(this).get(LoginViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        initView()
        initFacebookSdk()

        setButtonClickListeners()

        setLiveDataListeners()
    }

    private fun initView() {
        loginUsernameEditText.hint = DbHelper.getString(Const.LOGIN_EMAIL)
        loginPasswordEditText.hint = DbHelper.getString(Const.LOGIN_PASS)
        loginButton.text = DbHelper.getString(Const.LOGIN_LOGIN_BTN)

        tvNewCustomer.text = DbHelper.getString(Const.LOGIN_NEW_CUSTOMER)
        tvForgotPassword.text = DbHelper.getString(Const.LOGIN_FORGOT_PASS)
        tvLoginOr.text = DbHelper.getString(Const.LOGIN_OR)
        fbLoginButton.text = DbHelper.getString(Const.LOGIN_LOGIN_WITH_FB)
    }

    private fun setButtonClickListeners()
    {
        loginButton?.setOnClickListener {  sendLogInData();   requireActivity().hideKeyboard()   }

        tvNewCustomer.setOnClickListener { replaceFragmentSafely(RegisterFragment()) }

        tvForgotPassword?.setOnClickListener { replaceFragmentSafely(ForgotPasswordFragment()) }
    }

    private fun sendLogInData()
    {
        if (loginUsernameEditText?.text?.isNotEmpty() == true && loginPasswordEditText?.text?.isNotEmpty() == true)
        {
            if(loginUsernameEditText?.text.isEmailValid())
                (viewModel as LoginViewModel).postLoginVM(LoginPostData(LoginData(email = loginUsernameEditText?.text.toString(), password = loginPasswordEditText?.text?.toString().toString())), model)
            else
                toast(DbHelper.getString(Const.ENTER_VALID_EMAIL))
        }
        else
        {
            toast(DbHelper.getString(Const.LOGIN_EMAIL_REQ))
            toast(DbHelper.getString(Const.LOGIN_PASS_REQ))
        }
    }

    private fun setLiveDataListeners()
    {

        (viewModel as LoginViewModel).apply {

            loginResponseLD.observe(viewLifecycleOwner, Observer { loginResponse ->


                loginResponse?.loginData?.token?.let {

                    prefObject.setPrefs(PrefSingleton.TOKEN_KEY, it)
                    prefObject.setPrefs(PrefSingleton.IS_LOGGED_IN, true)
                    prefObject.setCustomerInfo(PrefSingleton.CUSTOMER_INFO,
                        loginResponse.loginData.customerInfo)

                    NetworkUtil.token = it

                    "token".showLog("token: " + NetworkUtil.token)

                    toast(DbHelper.getString(Const.LOGIN_LOGIN_SUCCESS))

                    // get appLandingSettings
                    mainViewModel.updatingAppSettings = true
                    mainViewModel.getAppSettings(MainModelImpl(requireContext()))
                }
                    ?:    toast(loginResponse?.errorsAsFormattedString.toString())
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (getRootLayout() != null && isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })

        }


        mainViewModel.appSettingsLD.observe(viewLifecycleOwner, Observer { settings ->

            settings.peekContent()?.let {

                if (mainViewModel.updatingAppSettings) {
                    mainViewModel.updatingAppSettings = false

                    // HomeFragment is never in backStack
                    // Pop all items from backStack will make HomeFragment visible
                    /*requireActivity().supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )*/

                    //it.stringResources = listOf()
                    DbHelper.memCache = it

                    requireActivity().finish()
                    startActivity(
                        Intent(requireActivity().applicationContext, MainActivity::class.java)
                            //.putExtra(MainActivity.KEY_APP_SETTINGS, it)
                    )
                }
            }
        })

        mainViewModel.isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->
            if(isShowLoader) blockingLoader.showDialog()
            else blockingLoader.hideDialog()
        })
    }

    private fun initFacebookSdk()
    {

        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext())
        callbackManager = CallbackManager.Factory.create()
        fbLoginButton?.setReadPermissions("email")

        fbLoginButton?.fragment = this

        fbLoginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult>
        {
            override fun onSuccess(loginResult: LoginResult)
            {
                Log.d("FacebookLogin", "From LoginButton")
                onFacebookLogin(loginResult)
            }

            override fun onCancel() = Unit
            override fun onError(error: FacebookException)
            {
                Log.d("FacebookLogin", error.message.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
    private fun onFacebookLogin(loginResult: LoginResult)
    {

        val graphRequest = GraphRequest.newMeRequest(loginResult.accessToken)
        { `object`, response ->

            try
            {
                FacebookLogin().apply {

                    userId = loginResult.accessToken.userId
                    accessToken = loginResult.accessToken.token

                    email = `object`.getString("email")
                    name = `object`.getString("name")

                    //RetroClient.api.loginUsingFaceBook(this).enqueue(CustomCB(view!!)) //TODO
                }
            }
            catch (e: JSONException)
            {
                e.printStackTrace()
                LoginManager.getInstance().logOut()
                Toast.makeText(FacebookSdk.getApplicationContext(), getString(R.string.email_permission_require), Toast.LENGTH_LONG).show()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()

    }

    /*private fun initView() {

        Picasso.with(activity)
            .load(R.drawable.app_splash_background)
            .fit()
            .into(backgroundImageView)


    }*/

}
