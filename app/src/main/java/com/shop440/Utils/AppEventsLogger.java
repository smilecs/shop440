package com.shop440.Utils;

import android.os.Bundle;

/**
 * Created by mmumene on 31/05/2017.
 */

public class AppEventsLogger {

    public static void logMainActivityOpenedEvent () {
        Application.logger().logEvent("MainActivity Opened");
    }
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void logItemSelectedEvent (String item_name, String shop_name) {
        Bundle params = new Bundle();
        params.putString("Item_name", item_name);
        params.putString("Shop_name", shop_name);
        Application.logger().logEvent("Item Selected", params);
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
        Application.logger().logEvent("Item download", params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public static void logItemShareEvent () {
        Application.logger().logEvent("Item Share");
    }
}
