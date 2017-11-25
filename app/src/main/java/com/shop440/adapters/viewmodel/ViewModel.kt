package com.shop440.adapters.viewmodel

import android.view.View
import com.shop440.adapters.viewholders.BaseViewHolder

/**
 * Created by mmumene on 19/11/2017.
 */
abstract class ViewModel(val title:String){
    abstract fun type() :Int
    abstract fun size():Int
}

interface TypeFactory{
 fun holder(type:Int, view: View) : BaseViewHolder<*>

}