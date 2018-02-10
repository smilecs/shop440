package com.shop440.typefactory

import android.view.View
import com.shop440.navigation.home.viewholders.BaseViewHolder

/**
 * Created by mmumene on 10/02/2018.
 */
interface TypeFactory{
    fun holder(type:Int, view: View) : BaseViewHolder<*>

}