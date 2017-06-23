package com.shop440.Utils;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by smilecs on 6/5/15.
 */
public class Application extends android.app.Application {
    private static Application sInstance;
    private static AppEventsLogger logger;

    public static Application getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static AppEventsLogger logger(){
        return logger;
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
