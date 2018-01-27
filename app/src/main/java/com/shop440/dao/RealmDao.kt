package com.shop440.dao

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by mmumene on 27/01/2018.
 */
fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
fun Realm.kartDao() : KartDao = KartDao(this)
