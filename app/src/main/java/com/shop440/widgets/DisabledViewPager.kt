package com.shop440.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by mmumene on 13/12/2017.
 */
class DisabledViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs){
    override fun onTouchEvent(ev: MotionEvent?): Boolean = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false
}