package com.shop440.utils

import android.os.Bundle

import com.shop440.Application

/**
 * Created by mmumene on 31/05/2017.
 */

object AppEventsLogger {

    fun logMainActivityOpenedEvent() {
        Application.logger()!!.logEvent("MainActivity Opened")
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    fun logItemSelectedEvent(item_name: String, shop_name: String) {
        val params = Bundle()
        params.putString("Item_name", item_name)
        params.putString("Shop_name", shop_name)
        Application.logger()!!.logEvent("Item Selected", params)
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    fun logItemDownloadEvent(item_name: String, shop_name: String, category: String) {
        val params = Bundle()
        params.putString("Item_name", item_name)
        params.putString("Shop_name", shop_name)
        params.putString("NewItemCategoryActivity", category)
        Application.logger()!!.logEvent("Item download", params)
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    fun logItemShareEvent() {
        Application.logger()!!.logEvent("Item Share")
    }
}
