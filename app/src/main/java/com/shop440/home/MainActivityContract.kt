package com.shop440.home

import com.shop440.adapters.viewmodel.ViewModel
import com.shop440.BasePresenter
import com.shop440.BaseView

/**
 * Created by mmumene on 09/09/2017.
 */

interface MainActivityContract{
    interface View : BaseView<Presenter>{
        fun productDataAvailable(homeSection: List<ViewModel>)
    }

    interface Presenter : BasePresenter {
        fun getProductFeedData()
    }
}
