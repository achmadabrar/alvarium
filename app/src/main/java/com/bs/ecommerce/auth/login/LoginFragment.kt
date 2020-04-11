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
import com.bs.ecommerce.auth.login.data.FacebookLogin
import com.bs.ecommerce.auth.login.data.LoginData
import com.bs.ecommerce.auth.register.RegisterFragment
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.main.model.AuthModel
import com.bs.ecommerce.main.model.AuthModelImpl
import com.bs.ecommerce.networking.NetworkUtil
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.hideKeyboard
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException


class LoginFragment : BaseFragment()
{

    private var callbackManager: CallbackManager? = null

    private lateinit var model: AuthModel

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun getRootLayout(): RelativeLayout = login_root_layout

    override fun createViewModel(): BaseViewModel = LoginViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        model = AuthModelImpl()

        viewModel  = ViewModelProvider(this).get(LoginViewModel::class.java)

        initView()

        initFacebookSdk()

        setButtonClickListeners()

        setLiveDataListeners()
    }

    private fun setButtonClickListeners()
    {
        loginButton?.setOnClickListener {  sendLogInData();   requireActivity().hideKeyboard()   }

        tvNewCustomer.setOnClickListener {

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutFrame,
                    RegisterFragment()
                )
                .addToBackStack(RegisterFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun sendLogInData()
    {
        if (loginUsernameEditText?.text?.isNotEmpty()!! && loginPasswordEditText?.text?.isNotEmpty()!!)
        {
            (viewModel as LoginViewModel).postLoginInfo(
                LoginPostData(LoginData(
                    email = loginUsernameEditText?.text.toString(),
                    password = loginPasswordEditText?.text?.toString().toString())), model)
        }
        else
        {
            toast(getString(R.string.username_password_require))
        }
    }

    private fun setLiveDataListeners()
    {

        (viewModel as LoginViewModel).loginResponseLD.observe(requireActivity(), Observer { loginResponse ->


            val token = loginResponse.loginData.token

            if(token.isNotEmpty())
            {
                prefObject.setPrefs(PrefSingleton.TOKEN_KEY, token)
                prefObject.setPrefs(PrefSingleton.LOGGED_PREFER_KEY, true)

                NetworkUtil.token = token

                "token".showLog("token: " + NetworkUtil.token)

                toast(getString(R.string.login_successful))

                //requireActivity().supportFragmentManager.popBackStack()
            }
            else
                toast(loginResponse?.errorsAsFormattedString.toString())
        })

        (viewModel as LoginViewModel).isLoadingLD.observe(requireActivity(), Observer { isShowLoader ->

            if (isShowLoader)
                showLoading()
            else
                hideLoading()
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

    private fun initView() {

        Picasso.with(activity)
            .load(R.drawable.app_splash_background)
            .fit()
            .into(backgroundImageView)


    }

}
