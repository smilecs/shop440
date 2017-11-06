package com.shop440.utils;

import android.os.Bundle;

import com.shop440.Application;

/**
 * Created by mmumene on 31/05/2017.
 */

public class AppEventsLogger {

    public static void logMainActivityOpenedEvent () {
        Application.Companion.logger().logEvent("MainActivity Opened");
    }
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void logItemSelectedEvent (String item_name, String shop_name) {
        Bundle params = new Bundle();
        params.putString("Item_name", item_name);
        params.putString("Shop_name", shop_name);
        Application.Companion.logger().logEvent("Item Selected", params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void logItemDownloadEvent (String item_name, String shop_name, String category) {
        Bundle params = new Bundle();
        params.putString("Item_name", item_name);
        params.putString("Shop_name", shop_name);
        params.putString("NewItemCategoryActivity", category);
        Application.Companion.logger().logEvent("Item download", params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void logItemShareEvent () {
        Application.Companion.logger().logEvent("Item Share");
    }
}
