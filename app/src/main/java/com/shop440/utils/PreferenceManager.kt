package com.shop440.utils

import com.bentech.android.appcommons.preference.Preference

/**
 * Created by mmumene on 26/11/2017.
 */

class PreferenceManager : Preference() {
    var token: String? = null
    var name: String? = null
    var image: String? = null
    var phone: String? = null
    fun persistToken(userToken: String) {
        PrefData.getPreferenceManager()?.apply {
            token = userToken
        }?.savePreference()
    }

    fun persistName(name: String) {
        PrefData.getPreferenceManager()?.apply {
            this.name = name
        }?.savePreference()

    }

    fun persistPhone(phone:String){
        PrefData.getPreferenceManager()?.apply {
            this.phone = phone
        }?.savePreference()
    }

    fun persistImage(image:String){
        PrefData.getPreferenceManager()?.apply {
            this.image = image
        }?.savePreference()
    }

    fun getSavedImage() = PrefData.getPreferenceManager()?.image

    fun getSavedPhone() = PrefData.getPreferenceManager()?.phone

    fun getSavedName() = PrefData.getPreferenceManager()?.name

    fun getSavedToken() = PrefData.getPreferenceManager()?.token

    object PrefData {
        fun getPreferenceManager(): PreferenceManager? = Preference.getPreference(PreferenceManager::class.java)
    }
}