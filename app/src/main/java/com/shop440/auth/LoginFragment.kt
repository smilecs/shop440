package com.shop440.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.api.NetModule
import com.shop440.api.Urls
import com.shop440.models.User
import com.shop440.R
import com.shop440.utils.ProgressHelper
import retrofit2.Retrofit

/**
 * A login screen that offers login via email/password.
 */
class LoginFragment : Fragment(), AuthContract.View {

    // UI references.
    override lateinit var presenter: AuthContract.Presenter
    lateinit var editor: SharedPreferences.Editor
    lateinit var progressDialog:android.app.ProgressDialog
    lateinit var retrofit: Retrofit


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofit = NetModule.provideRetrofit()
        // Set up the login form.
        AuthPresenter(this, retrofit)
        progressDialog = ProgressHelper.progressDialog(context)
    }

    override fun saveUser(user: User) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(resources.getString(R.string.shop440), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(resources.getString(R.string.username), user.name)
        editor.putString(Urls.TOKEN, user.token)
        editor.apply()
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
      //  Snackbar.make(sign_in_rootView, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}

