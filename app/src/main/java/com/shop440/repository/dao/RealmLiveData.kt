package com.shop440.repository.dao

import android.arch.lifecycle.LiveData
import io.realm.*


/**
 * Created by mmumene on 27/01/2018.
 */
class RealmLiveData <T : RealmModel>(val realmResults: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        realmResults.addChangeListener(listener)
    }

    override fun onInactive() {
        realmResults.removeChangeListener(listener)
    }

    object SingleLiveData{
        class LiveObject<T : RealmObject>(val result:RealmObject):LiveData<T>(){

            private val listener = RealmChangeListener<T> { results -> value = results }

            override fun onActive() {
                result.addChangeListener(listener)
            }



            override fun onInactive() {
                result.removeChangeListener(listener)
            }
        }
    }
}