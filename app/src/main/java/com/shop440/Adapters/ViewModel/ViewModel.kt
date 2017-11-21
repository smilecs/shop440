package com.shop440.Adapters.ViewModel

import com.shop440.models.Feed

/**
 * Created by mmumene on 19/11/2017.
 */
abstract class ViewModel(val title:String){
    abstract fun type() :Int
}

interface TypeFactory{
 fun type(feed: Feed.ProductFeed) : Int
 fun type(feed: Feed.StoreFeed) : Int
}