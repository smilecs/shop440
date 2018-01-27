package com.shop440.productview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.shop440.cart.Item
import com.shop440.dao.kartDao
import com.shop440.dao.models.ProductFeed
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 27/01/2018.
 */
open class ProductViewModel : ViewModel() {
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getKartData(): LiveData<RealmResults<Item>> {
        return realm.kartDao().getKart()
    }

    fun addToKart(productFeed: ProductFeed){
        realm.kartDao().addToKart(productFeed)
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}