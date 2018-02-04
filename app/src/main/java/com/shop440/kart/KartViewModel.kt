package com.shop440.kart

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.shop440.dao.kartDao
import com.shop440.dao.models.ProductFeed
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 27/01/2018.
 */
open class KartViewModel : ViewModel() {
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getKartData(): LiveData<RealmResults<Item>> {
        return realm.kartDao().getKart()
    }

    fun addToKart(productFeed: ProductFeed){
        realm.kartDao().addToKart(productFeed)
    }

    fun addToKart(productFeed: ItemForKart){
        realm.kartDao().addToKart(productFeed)
    }

    fun deleteFromKart(id:String){
        realm.kartDao().deleteItemFromKart(id)
    }

    fun deleteAll(slug:String){
        realm.kartDao().deleteAll(slug)
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}