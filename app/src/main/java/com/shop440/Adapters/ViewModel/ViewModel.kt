package com.shop440.Adapters.ViewModel

import android.view.View
import com.shop440.Adapters.ViewHolders.BaseViewHolder
import com.shop440.models.Feed
import com.shop440.models.ProductFeed
import com.shop440.models.StoreFeed

/**
 * Created by mmumene on 19/11/2017.
 */
abstract class ViewModel(val title:String){
    abstract fun type(types: TypeFactory) :Int
}

interface TypeFactory{
 fun type(type: Int) : Int
 fun holder(type:Int, view: View) : BaseViewHolder<*>

}