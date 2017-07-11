package com.shop440.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.appevents.AppEventsLogger;
import com.shop440.R;

/**
 * Created by smilecs on 6/5/15.
 */
public class Application extends android.app.Application {
    private static Application sInstance;
    private static AppEventsLogger logger;
    private static String PATH = "fonts/";

    public static Application getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static AppEventsLogger logger() {
        return logger;
    }

    public static Typeface getFont(int id) {
        Typeface typeface = null;
        String fontId = sInstance.getResources().getStringArray(R.array.fonts)[id];
        fontId = PATH.concat(fontId);
        try {
            typeface = Typeface.createFromAsset(sInstance.getAssets(), fontId);
        } catch (Exception e) {
            Log.d("App", "Could not create font: " + fontId);
        }
        return typeface;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AccountKit.initialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        logger = AppEventsLogger.newLogger(this);
    }

}
