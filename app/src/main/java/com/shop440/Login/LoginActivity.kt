package com.shop440.Login

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.RequestQueue
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.*
import com.shop440.Api.NetModule
import com.shop440.Models.User
import com.shop440.R
import com.shop440.Utils.ProgressDialog
import com.shop440.Api.Urls
import com.shop440.Utils.VolleySingleton
import kotlinx.android.synthetic.main.sign_in.*
import retrofit2.Retrofit

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginContract.View {

    // UI references.
    override lateinit var presenter: LoginContract.Presenter
    lateinit var editor: SharedPreferences.Editor
    lateinit var volleySingleton: VolleySingleton
    lateinit var queue: RequestQueue
    lateinit var progressDialog:android.app.ProgressDialog
    var email: String = ""
    var name: String = ""
    var newUser: Boolean = true
    val APP_REQUEST_CODE: Int = 104
    lateinit var retrofit: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        volleySingleton = VolleySingleton.getsInstance()
        retrofit = NetModule.provideRetrofit()
        // Set up the login form.
        LoginPresenter(this, retrofit)
        login_activity_signup_button.setOnClickListener(signUplistener())
        login_activity_signup_button_ok.setOnClickListener(confirmSignUp())
        login_activity_sign_in_button.setOnClickListener(login())
        queue = volleySingleton.getmRequestQueue()
        progressDialog = ProgressDialog.progressDialog(this)
    }

    fun phoneLogin() {
        val intent = Intent(this, AccountKitActivity::class.java)
        val configBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN)
        val uiManager: UIManager = SkinManager(SkinManager.Skin.TRANSLUCENT, R.color.colorPrimaryDark)
        configBuilder.setUIManager(uiManager)
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configBuilder.build())
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_REQUEST_CODE) {
            val loginResult: AccountKitLoginResult = data!!.getParcelableExtra(AccountKitLoginResult.RESULT_KEY)
            if (loginResult.error != null) {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                return
            }
            userAuthFinished()
        }
    }

    private fun userAuthFinished() {
        val user:User = User()
        if (newUser) {
            user.name = name
            user.email = email
        }
        getCurrentAccountFromKit(user)
    }

    private fun signUplistener(): View.OnClickListener {
        return View.OnClickListener {
            newUser = true
            val layoutTransition: LayoutTransition = sign_up_button_container.layoutTransition
            layoutTransition.setDuration(400)
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            if (sign_up_form.visibility == View.VISIBLE) {
                sign_up_form.visibility = View.GONE
                sign_in_container.visibility = View.VISIBLE
            } else {
                sign_in_container.visibility = View.INVISIBLE
                sign_up_form.visibility = View.VISIBLE
            }
        }
    }

    private fun confirmSignUp(): View.OnClickListener {
        return View.OnClickListener {
            email = login_activity_email.text.toString()
            name = login_activity_name.text.toString()
            newUser = true
            phoneLogin()
        }
    }

    private fun login(): View.OnClickListener {
        return View.OnClickListener {
            newUser = false
            sign_up_form.visibility = View.GONE
            val user:User = User()
            user.phone = "07033383068"
            user.name = "name"
            user.email = "mumene@gmail.com"
            phoneLogin()
        }
    }

    override fun saveUser(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.username), user.name)
        editor.putString(Urls.TOKEN, user.token)
        editor.apply()
        finish()
    }

    override fun onDataLoading() {
        if(progressDialog.isShowing){
            progressDialog.hide()
            return
        }
        progressDialog.show()
    }

    override fun onError(errorMessage: Int) {
        Snackbar.make(sign_in_rootView, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    fun getCurrentAccountFromKit(user: User){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(account: Account?) {
                Log.i("LoginActivity", account?.phoneNumber.toString())
                user.phone = account?.phoneNumber.toString()
                if(newUser){
                    presenter.signUp(user)
                }else{
                    presenter.login(user)
                }
            }

            override fun onError(error: AccountKitError?) {
                Log.i("LoginActivity", error.toString())
            }
        })
    }
}

