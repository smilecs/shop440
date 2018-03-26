package com.shop440.features.auth

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bentech.android.appcommons.utils.EditTextUtils
import com.bentech.android.appcommons.validator.EditTextPhoneNumberValidator
import com.bentech.android.appcommons.validator.EditTextRequiredInputValidator
import com.shop440.R
import com.shop440.repository.api.NetModule
import com.shop440.repository.dao.models.User
import com.shop440.utils.PreferenceManager
import com.shop440.utils.ProgressHelper
import kotlinx.android.synthetic.main.sign_in.*

/**
 * A login screen that offers login via email/password.
 */
class LoginFragment : Fragment(), AuthContract.View {

    // UI references.
    override lateinit var presenter: AuthContract.Presenter
    private val progressDialog: android.app.ProgressDialog by lazy {
        ProgressHelper.progressDialog(context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up the login form.
        AuthPresenter(this, NetModule.provideRetrofit())
        loginPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        closeAuthButton.setOnClickListener {
            activity.setResult(Activity.RESULT_CANCELED)
            activity.finish()
        }

        signInButton.setOnClickListener {
            if (EditTextUtils.isInValid(EditTextPhoneNumberValidator(loginPhone),
                    EditTextRequiredInputValidator(loginPassword))) {
                return@setOnClickListener
            }
            val user = User()
            user.phone = loginPhone.text.toString().replace(" ", "")
            user.password = loginPassword.text.toString()
            presenter.login(user)
        }
    }


    override fun saveUser(user: User) {
        PreferenceManager.PrefData.getPreferenceManager()?.apply {
            persistName(user.name)
            persistToken(user.token)
            persistImage(user.image)
            persistPhone(user.phone)
        }
        progressDialog.dismiss()
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    override fun onDataLoading() {
        if (progressDialog.isShowing) {
            progressDialog.hide()
            return
        } else if (isVisible) {
            progressDialog.show()
        }
    }

    override fun onError(errorMessage: Int) {
        view?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG).show()
        }

    }
}

