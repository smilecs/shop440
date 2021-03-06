package com.shop440.features.navigation.home

import com.shop440.features.navigation.home.adaptermodel.AdapterModel
import com.shop440.BasePresenter
import com.shop440.BaseView

/**
 * Created by mmumene on 09/09/2017.
 */

interface HomeActivityContract {
    interface View : BaseView<Presenter>{
        fun productDataAvailable(homeSection: List<AdapterModel>)
    }

    interface Presenter : BasePresenter {
        fun getProductFeedData()
    }
}
