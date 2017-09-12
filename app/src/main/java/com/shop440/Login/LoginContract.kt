package com.shop440.Login

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.Models.User

/**
 * Created by mmumene on 25/08/2017.
 */
interface LoginContract {
    interface View : BaseView<Presenter> {
        fun saveUser(user: User)
    }

    interface Presenter : BasePresenter {
        fun login(user: User)
        fun signUp(user: User)
        fun onAuthComplete(isNewUser: Boolean)
    }
}