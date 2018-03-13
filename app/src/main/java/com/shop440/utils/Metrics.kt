package com.shop440.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import com.shop440.R

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

    fun getDisplayPriceWithCurrency(context: Context, price: Int): String = String.format("%s %,d", context.getString(R.string.currency_symbol_code), price)

    fun getDisplayPriceWithCurrency(context: Context, price: Double): String = String.format("%s %,.2f", context.getString(R.string.currency_symbol_code), price)
}
