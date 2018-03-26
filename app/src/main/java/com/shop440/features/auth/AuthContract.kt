package com.shop440.features.auth

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.repository.dao.models.User

/**
 * Created by mmumene on 25/08/2017.
 */
interface AuthContract {
    interface View : BaseView<Presenter> {
        fun saveUser(user: User)
    }

    interface Presenter : BasePresenter {
        fun login(user: User)
        fun signUp(user: User)
        fun onAuthComplete(isNewUser: Boolean)
        fun checkExisting(user: User)
        fun onRequestOtp(phone: String, otpListener: OtpListener)
    }

    interface OtpListener{
        fun onOtpReceived(otp:String?)
    }
}