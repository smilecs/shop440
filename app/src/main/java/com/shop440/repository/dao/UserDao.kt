package com.shop440.repository.dao

import com.shop440.repository.dao.models.UserAdress
import io.realm.Realm

/**
 * Created by mmumene on 18/02/2018.
 */
class UserDao(val realm: Realm) {
    fun addAddress(userAdress: UserAdress) {
        /*realm.executeTransactionAsync {
            it.insert(userAdress)
        }*/
    }

    /*fun getUserAddress(): LiveData<UserAdress> {
        return realm.where(UserAdress::class.java).findFirstAsync().toLiveObject()
    }*/
}