package com.shop440.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast

import com.shop440.models.User
import com.shop440.receiver.SmsReciever

import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.api.Urls
import kotlinx.android.synthetic.main.confirmation.*

class VerifyActivity : AppCompatActivity(), AuthContract.View {
    override lateinit var presenter: AuthContract.Presenter
    private var TAG = "Confirm.kt"
    private lateinit var user: User
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation)
        sharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        user = intent.getSerializableExtra("data") as User
        AuthPresenter(this, NetModule.provideRetrofit())
        Log.d(TAG, user.name)
        SmsReciever.bindListener { messageText ->
            Log.d("Text", messageText)
            Toast.makeText(this@VerifyActivity, getString(R.string.passcode_loading_text), Toast.LENGTH_LONG).show()
            val newString = messageText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[5].replace(".", "")
            if (newString == otp) {
                createUser()
            }
        }
        otpText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start >= 1) {
                    continueButton.isEnabled = true
                }
            }
        })
        continueButton.setOnClickListener({ _ ->
            if(otpText.text.toString().equals(otp, true)){
                createUser()
            }
        })
        presenter.onRequestOtp(user.phone, object : AuthContract.OtpListener {
            override fun onOtpReceived(otp: String) {
                this@VerifyActivity.otp = otp
            }
        })
    }

    override fun onError(errorMessage: Int) {
    }

    override fun onDataLoading() {
    }

    override fun saveUser(user: User) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.username), user.name)
        editor.putString(Urls.TOKEN, user.token)
        editor.apply()
        finish()
    }

    private fun createUser() {
        presenter.signUp(user)
    }
}
