package com.shop440.auth

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.truecaller.android.sdk.*
import kotlinx.android.synthetic.main.auth_options.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_container)
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            replace(R.id.content, AuthOptions(), "options")
        }.commit()
    }

    internal class AuthOptions : Fragment(), ITrueCallback {
        val trueClient: TrueClient by lazy {
            TrueClient(context, this)
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater?.inflate(R.layout.auth_options, container, false)
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            val trueButton = view?.findViewById(R.id.com_truecaller_android_sdk_truebutton) as (TrueButton)
            trueButton.visibility = if (trueButton.isUsable) {
                View.VISIBLE
            } else {
                View.GONE
            }
            trueButton.setTrueClient(trueClient)
            createAccountAuthButton.setOnClickListener {
                fragmentManager.beginTransaction().apply {
                    setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    addToBackStack(null)
                    replace(R.id.content, SignupFragment(), "signUp")
                }.commit()
            }
            loginButtonAuth.setOnClickListener {
                fragmentManager.beginTransaction().apply {
                    addToBackStack(null)
                    replace(R.id.content, LoginFragment(), "login")
                }.commit()
            }
        }

        override fun onSuccesProfileShared(p0: TrueProfile) {

        }

        override fun onFailureProfileShared(p0: TrueError) {

        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (trueClient.onActivityResult(requestCode, resultCode, data)) {
                return
            }
        }
    }
}
