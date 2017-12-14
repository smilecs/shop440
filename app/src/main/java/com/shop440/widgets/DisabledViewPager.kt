package com.shop440.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * Created by mmumene on 13/12/2017.
 */
class DisabledViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs){
    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }
}