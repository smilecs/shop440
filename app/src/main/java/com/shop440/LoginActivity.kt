package com.shop440

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.*
import com.shop440.Utils.Urls
import com.shop440.Utils.VolleySingleton
import kotlinx.android.synthetic.main.sign_in.*
import org.json.JSONObject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    // UI references.
    lateinit var editor: SharedPreferences.Editor
    lateinit var volleySingleton: VolleySingleton
    lateinit var queue: RequestQueue
    var email: String = ""
    var name: String = ""
    var newUser: Boolean = true
    val APP_REQUEST_CODE: Int = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        volleySingleton = VolleySingleton.getsInstance()
        // Set up the login form.

        login_activity_signup_button.setOnClickListener(signUplistener())
        login_activity_signup_button_ok.setOnClickListener(confirmSignUp())
        login_activity_sign_in_button.setOnClickListener(login())
        queue = volleySingleton.getmRequestQueue()
    }

    fun phoneLogin() {
        val intent = Intent(this, AccountKitActivity::class.java)
        val configBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN)
        var uiManager: UIManager = SkinManager(SkinManager.Skin.TRANSLUCENT, R.color.colorPrimaryDark)
        configBuilder.setUIManager(uiManager)
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configBuilder.build())
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_REQUEST_CODE) {
            val loginResult: AccountKitLoginResult = data!!.getParcelableExtra(AccountKitLoginResult.RESULT_KEY)
            Log.i("AccountKit", "accountkit")
            if (loginResult.error != null) {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                return
            }
            userAuthFinished()
        }
    }

    private fun userAuthFinished() {
        val login: JSONObject = JSONObject()
        var url: String
        if (newUser) {
            url = Urls.NEW_USER
            login.put("Name", name)
            login.put("Email", email)
        } else {
            url = Urls.LOGIN
        }
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(account: Account?) {
                login.put("Phone", account?.phoneNumber)
            }

            override fun onError(error: AccountKitError?) {
                Log.i("LoginActivity", error.toString())
            }
        })
        getTokenHandler(login, url)
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

    private fun getTokenHandler(login: JSONObject, url: String) {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + url, login, Response.Listener<JSONObject> { response ->
            try {
                Log.d("response", response.toString())
                val sharedPreferences: SharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
                editor = sharedPreferences.edit()
                editor.putString(Urls.TOKEN, response.getString("Token"))
                editor.apply()
                val i = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(3 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(jsonObjectRequest)
    }

    private fun login(): View.OnClickListener{
        return View.OnClickListener {
            newUser = false
            sign_up_form.visibility = View.GONE
            phoneLogin()
        }
    }
}

