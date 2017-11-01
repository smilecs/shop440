package com.shop440.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shop440.R
import kotlinx.android.synthetic.main.auth_container.*
import kotlinx.android.synthetic.main.signup.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_container)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        loginButtonAuth.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                addToBackStack(null)
                replace(R.id.authContainer, LoginFragment(), "login")
            }.commit()
        }

        signupButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                addToBackStack(null)
                replace(R.id.authContainer, SignupFragment(), "signUp")
            }.commit()
        }

    }
}
