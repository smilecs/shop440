package com.shop440.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bentech.android.appcommons.utils.EditTextUtils
import com.bentech.android.appcommons.validator.EditTextEmailValidator
import com.bentech.android.appcommons.validator.EditTextPhoneNumberValidator
import com.bentech.android.appcommons.validator.EditTextRequiredInputValidator
import com.shop440.models.User
import com.shop440.R
import kotlinx.android.synthetic.main.signup.*

class SignupFragment : Fragment(), AuthContract.View {

    override lateinit var presenter: AuthContract.Presenter
    private var progressDialog:ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.signup, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        progressDialog = com.shop440.utils.ProgressDialog.progressDialog(context)
        signupButton.setOnClickListener {
            if(EditTextUtils.isInValid(EditTextRequiredInputValidator(nameSignUp),
                    EditTextPhoneNumberValidator(phoneSignUp), EditTextEmailValidator(emailSignUp))){
                return@setOnClickListener
            }
            val user = User()
            user.name = nameSignUp.text.toString()
            user.phone = phoneSignUp.text.toString()
            user.email = emailSignUp.text.toString()
            saveUser(user)
        }

    }

    override fun onError(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDataLoading() {
        if(progressDialog?.isShowing!!){
            progressDialog?.dismiss()
        }else{
            progressDialog?.show()
        }

    }

    override fun saveUser(user: User) {
        val intent = Intent(context, VerifyActivity::class.java)
        intent.putExtra("data", user)
        startActivity(intent)
        activity.finish()
    }
}
