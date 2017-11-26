package com.shop440.utils

import com.bentech.android.appcommons.preference.Preference

/**
 * Created by mmumene on 26/11/2017.
 */

class PreferenceManager : Preference() {
    var token: String? = null
    var name:String? = null
    fun persistToken(userToken: String) {
        PrefData.getPreferenceManager()?.token = userToken
    }

    fun persistName(name:String){
        PrefData.getPreferenceManager()?.name = name
    }


    object PrefData{
        fun getPreferenceManager():PreferenceManager? = Preference.getPreference(PreferenceManager::class.java)
    }
}