package com.shop440.viewmodel

import android.arch.lifecycle.ViewModel
import io.realm.Realm

/**
 * Created by mmumene on 18/02/2018.
 */
class UserViewModel : ViewModel(){
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}