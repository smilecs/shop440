package com.shop440.Utils;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by smilecs on 6/5/15.
 */
public class Application extends android.app.Application {
	private static Application sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
	}
	public static Application getsInstance(){
		return sInstance;
	}
	public static Context getAppContext(){
		return sInstance.getApplicationContext();
	}
}
