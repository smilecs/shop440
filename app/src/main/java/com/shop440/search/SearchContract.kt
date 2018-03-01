package com.shop440.search

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.response.FilterResponse

/**
 * Created by mmumene on 25/02/2018.
 */
interface SearchContract{

    interface View : BaseView<Presenter>{
        fun onSearchResults(filter:FilterResponse)
    }

    interface Presenter : BasePresenter{
        fun performSearch(q:String, p:String, cat:String, tag:String)
    }
}