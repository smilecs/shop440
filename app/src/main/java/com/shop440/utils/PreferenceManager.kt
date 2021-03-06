package com.shop440.utils

import com.bentech.android.appcommons.preference.Preference
import java.util.*

/**
 * Created by mmumene on 26/11/2017.
 */

class PreferenceManager : Preference() {
    var token: String? = null
    var name: String? = null
    var image: String? = null
    var phone: String? = null
    var city: String? = null
    var address: String? = null
    var deviceId : String = ""
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

    fun persistCity(city:String){
        PrefData.getPreferenceManager()?.apply {
            this.city = city
        }?.savePreference()
    }

    fun persistAddress(address:String){
        PrefData.getPreferenceManager()?.apply {
            this.address = address
        }?.savePreference()
    }

    fun persistDeviceId():String{
        val uid = UUID.randomUUID().toString()
        PrefData.getPreferenceManager()?.apply {
            this.deviceId = uid
        }?.savePreference()
        return uid
    }

    object PrefData {
        fun getPreferenceManager(): PreferenceManager? = Preference.getPreference(PreferenceManager::class.java)
    }
}