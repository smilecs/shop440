package com.shop440.Utils

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics

/**
 * Created by SMILECS on 2/13/17.
 */

object Metrics {
    fun GetMetrics(list: RecyclerView, activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val cardWidth = metrics.xdpi.toInt()
        return Math.floor((list.context.resources.displayMetrics.widthPixels / cardWidth.toFloat()).toDouble()).toInt()
    }
}
