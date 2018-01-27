package com.shop440.productview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.shop440.cart.ShopOrders
import com.shop440.dao.kartDao
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by mmumene on 27/01/2018.
 */
open class ProductViewModel : ViewModel() {
    private var kartData = MutableLiveData<RealmResults<ShopOrders>>()
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun getKartData(): MutableLiveData<RealmResults<ShopOrders>> {
        return kartData.apply {
            value = realm.kartDao().getKart().value
        }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}