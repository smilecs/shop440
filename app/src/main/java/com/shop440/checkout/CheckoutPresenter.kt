package com.shop440.checkout

import retrofit2.Retrofit

/**
 * Created by mmumene on 03/02/2018.
 */
class CheckoutPresenter(view: CheckoutContract.View, retrofit: Retrofit): CheckoutContract.Presenter{

    init {
        view.presenter = this
    }

    override fun start() {

    }

    override fun checkOut() {

    }
}