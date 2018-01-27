package com.shop440.dao

import android.arch.lifecycle.LiveData
import com.shop440.cart.Item
import com.shop440.cart.ShopOrders
import com.shop440.dao.models.ProductFeed
import io.realm.Realm
import io.realm.RealmResults


/**
 * Created by mmumene on 27/01/2018.
 */
class KartDao(val realm: Realm) {

    fun addToKart(productFeed: ProductFeed) {
        realm.executeTransactionAsync {
            val shopObject = it.where(ShopOrders::class.java).equalTo("shopId", productFeed.shopId).findFirst() ?: it.createObject(ShopOrders::class.java, productFeed.shopId).apply {
                shopId = productFeed.shopId
                shopName = productFeed.shop.title
            }
            shopObject.itemCost += productFeed.productPrice
            val item = it.createObject(Item::class.java)
            item.totalPrice = productFeed.productPrice
            item.slug = productFeed.slug
            shopObject.items.add(item)
        }
    }

    fun getKart(): LiveData<RealmResults<ShopOrders>> {
        return realm.where(ShopOrders::class.java).findAllAsync().asLiveData()
    }

}