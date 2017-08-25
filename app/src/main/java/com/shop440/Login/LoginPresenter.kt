package com.shop440.Login

import com.shop440.Models.User

/**
 * Created by mmumene on 25/08/2017.
 */

class LoginPresenter(val loginView:LoginContract.View):LoginContract.Presenter{
    init {
        loginView.presenter = this
    }
    override fun start() {
        loginView.showFeedBack()
    }

    override fun login(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthComplete(isNewUser: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}