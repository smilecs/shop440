package com.shop440.checkout

import android.arch.lifecycle.ViewModel
import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.checkout.models.Order
import com.shop440.viewmodel.UserViewModel

/**
 * Created by mmumene on 03/02/2018.
 */
interface CheckoutContract{

    interface View : BaseView<Presenter>{
        fun onCheckOut()
        fun getViewModel():ViewModel
    }

    interface Presenter : BasePresenter{
        fun checkOut(order:Order)
    }
}