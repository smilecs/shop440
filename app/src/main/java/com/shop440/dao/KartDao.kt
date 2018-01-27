package com.shop440.dao

import android.arch.lifecycle.LiveData
import com.shop440.cart.Item
import com.shop440.dao.models.ProductFeed
import io.realm.Realm
import io.realm.RealmResults


/**
 * Created by mmumene on 27/01/2018.
 */
class KartDao(val realm: Realm) {

    fun addToKart(productFeed: ProductFeed) {
        realm.executeTransactionAsync {
            val item = Item()
            item.totalPrice = productFeed.productPrice
            item.slug = productFeed.slug
            it.insert(item)
        }
    }

    fun getKart(): LiveData<RealmResults<Item>> {
        return realm.where(Item::class.java).findAllAsync().asLiveData()
    }

}