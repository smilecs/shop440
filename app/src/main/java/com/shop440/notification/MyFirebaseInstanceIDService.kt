package com.shop440.notification

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.shop440.repository.api.NetModule
import com.shop440.repository.api.response.GenericResponse
import com.shop440.utils.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by mmumene on 26/03/2018.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(this.javaClass.name, "Refreshed token: " + refreshedToken!!)
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String) {
        val mod = NetModule.provideRetrofit().create(ApiRequest::class.java)
        PreferenceManager.PrefData.getPreferenceManager()?.let {
            val uid = if(it.deviceId.isNotBlank()){
                it.deviceId
            }else{
                it.persistDeviceId()
            }
            mod.subscribe(DeviceModel(uid, token)).enqueue(object : Callback<GenericResponse> {
                override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                    t?.printStackTrace()
                }

                override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {

                }
            })
        }
    }
}