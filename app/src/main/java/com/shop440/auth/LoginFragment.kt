package com.shop440.auth

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.api.NetModule
import com.shop440.models.User
import com.shop440.utils.PreferenceManager
import com.shop440.utils.ProgressHelper

/**
 * A login screen that offers login via email/password.
 */
class LoginFragment : Fragment(), AuthContract.View {

    // UI references.
    override lateinit var presenter: AuthContract.Presenter
    private val progressDialog:android.app.ProgressDialog by lazy {
        ProgressHelper.progressDialog(context)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up the login form.
        AuthPresenter(this, NetModule.provideRetrofit())
    }

    override fun saveUser(user: User) {
        PreferenceManager.PrefData.getPreferenceManager()?.apply {
            persistName(user.name)
            persistToken(user.token)
            persistImage(user.image)
            persistPhone(user.phone)
        }
        activity.finish()
    }

    override fun onDataLoading() {
        if(progressDialog.isShowing){
            progressDialog.hide()
            return
        }
        progressDialog.show()
    }

    override fun onError(errorMessage: Int) {
        view?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG).show()
        }

    }
}

