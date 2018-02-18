package com.shop440.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.shop440.dao.UserDao
import com.shop440.dao.models.UserAdress
import io.realm.Realm

/**
 * Created by mmumene on 18/02/2018.
 */
class UserViewModel : ViewModel(){
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    fun addAddress(userAdress: UserAdress){
        realm.UserDao().addAddress(userAdress)
    }

    fun getUserAddress() : LiveData<UserAdress>{
        return realm.UserDao().getUserAddress()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}