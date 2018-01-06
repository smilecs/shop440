package com.shop440

import android.content.Context
import android.graphics.Typeface
import android.support.multidex.MultiDex
import android.util.Log
import com.bentech.android.appcommons.AppCommons
import com.facebook.FacebookSdk
import com.facebook.accountkit.AccountKit
import com.facebook.appevents.AppEventsLogger
import com.shop440.api.Urls
import io.realm.Realm

/**
 * Created by smilecs on 6/5/15.
 */
class Application : android.app.Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        AppCommons.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        Realm.init(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AccountKit.initialize(applicationContext)
        AppEventsLogger.activateApp(this)
        logger = AppEventsLogger.newLogger(this)
        AppCommons.getAppCommonsConfiguration().editTextInvalidEmailErrorMessage = R.string.email_invalid_error
        AppCommons.getAppCommonsConfiguration().editTextPhoneNumberValidatorErrorMessage = R.string.phone_invalid_error_message
        AppCommons.getAppCommonsConfiguration().editTextRequiredErrorMessage = R.string.required_field_error
    }

    companion object {
        private var sInstance: Application? = null
        private var logger: AppEventsLogger? = null
        private val PATH = "fonts/"

        fun getsInstance(): Application? = sInstance

        val appContext: Context
            get() = sInstance?.applicationContext!!

        fun logger(): AppEventsLogger? = logger

        fun getFont(id: Int): Typeface? {
            var typeface: Typeface? = null
            var fontId = sInstance?.resources!!.getStringArray(R.array.fonts)[id]
            fontId = PATH + fontId
            try {
                typeface = Typeface.createFromAsset(sInstance!!.assets, fontId)
            } catch (e: Exception) {
                Log.d("App", "Could not create font: " + fontId)
            }
            return typeface
        }

        val authToken: String
            get() {
                val preferences = getsInstance()!!.getSharedPreferences(getsInstance()!!.getString(R.string.shop440), Context.MODE_PRIVATE)
                return preferences.getString(Urls.TOKEN, "null")
            }
    }


}
