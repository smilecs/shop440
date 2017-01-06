package com.shop440.Utils;

import android.content.Context;

/**
 * Created by smilecs on 6/5/15.
 */
public class Application extends android.app.Application {
	private static Application sInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		sInstance = this;
	}
	public static Application getsInstance(){
		return sInstance;
	}
	public static Context getAppContext(){
		return sInstance.getApplicationContext();
	}
}
