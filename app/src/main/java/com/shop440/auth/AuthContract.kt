package com.shop440.auth

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.models.User

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
        fun checkExisting(user:User)
        fun onRequestOtp(phone: String, otpListener: OtpListener)
    }

    interface OtpListener{
        fun onOtpReceived(otp:String?)
    }
}