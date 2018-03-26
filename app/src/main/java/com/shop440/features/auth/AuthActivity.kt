package com.shop440.features.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shop440.R
import com.shop440.repository.dao.models.User
import com.truecaller.android.sdk.*
import kotlinx.android.synthetic.main.auth_options.*

class AuthActivity : AppCompatActivity() {
    private val authOptions = AuthOptions()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_container)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content, authOptions, "options")
        }.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authOptions.onActivityResult(requestCode, resultCode, data)
    }

    internal class AuthOptions : Fragment(), ITrueCallback {
        val trueClient: TrueClient by lazy {
            TrueClient(context, this)
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater?.inflate(R.layout.auth_options, container, false)
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            val trueButton = view?.findViewById<TrueButton>(R.id.com_truecaller_android_sdk_truebutton) as (TrueButton)
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
            val user = User()
            user.phone = p0.phoneNumber
            user.name = p0.firstName + " " + p0.lastName
            user.email = p0.email
            user.imageUrl = p0.avatarUrl
            val bundle = Bundle()
            bundle.putSerializable("data", user)
            val signUp = SignupFragment()
            signUp.arguments = bundle
            fragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                addToBackStack(null)
                replace(R.id.content, signUp, "signUp")
            }.commit()
        }

        override fun onFailureProfileShared(p0: TrueError) {
            Toast.makeText(context, getString(R.string.sign_in_error), Toast.LENGTH_SHORT).show()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (trueClient.onActivityResult(requestCode, resultCode, data)) {
                return
            } else if(requestCode == 200){
                activity.setResult(Activity.RESULT_OK)
                activity.finish()
            }
        }
    }
}
