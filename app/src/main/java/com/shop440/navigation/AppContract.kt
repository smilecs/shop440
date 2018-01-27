package com.shop440.navigation

import com.shop440.BasePresenter
import com.shop440.BaseView

/**
 * Created by mmumene on 31/12/2017.
 */
interface AppContract{

    interface AppView : BaseView<Presenter>{

    }

    interface Presenter : BasePresenter{
    }
}