package com.shop440.checkout

import com.shop440.BasePresenter
import com.shop440.BaseView

/**
 * Created by mmumene on 03/02/2018.
 */
interface CheckoutContract{

    interface View : BaseView<Presenter>{
        fun onCheckOut()
    }

    interface Presenter : BasePresenter{
        fun checkOut()
    }
}