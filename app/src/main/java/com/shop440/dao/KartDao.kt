package com.shop440.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.shop440.checkout.models.Item
import com.shop440.dao.models.ProductFeed
import com.shop440.checkout.models.ItemForKart
import com.shop440.checkout.models.Order
import io.realm.Realm
import io.realm.RealmResults
import java.util.*


/**
 * Created by mmumene on 27/01/2018.
 */
class KartDao(val realm: Realm) {

    fun addToKart(productFeed: ProductFeed) {
        realm.executeTransactionAsync {
            val item = Item()
            item.totalPrice = productFeed.productPrice
            item.slug = productFeed.slug
            item.shopName = productFeed.shop.title
            item.shopSlug = productFeed.shop.shopId
            item.itemName = productFeed.productName
            item.id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            it.insert(item)
        }
    }

    fun persistOrder(order: Order) : LiveData<Order>{
        val liveData = MutableLiveData<Order>()
        realm.executeTransactionAsync { obj ->
            val ob = obj.copyToRealm(order)
            liveData.postValue(ob)
        }
        return liveData
    }

    fun addToKart(item: ItemForKart){
        realm.executeTransactionAsync {
            val newItem = Item().apply {
                totalPrice = item.amount
                slug = item.slug
                shopName = item.shopName
                itemName = item.itemName
                id = Calendar.getInstance().timeInMillis.toString() + " " + UUID.randomUUID().toString()
            }
            it.insert(newItem)
        }
    }

    fun getKart(): LiveData<RealmResults<Item>> {
        return realm.where(Item::class.java).findAllAsync().asLiveData()
    }

    fun deleteItemFromKart(id: String) {
        realm.executeTransactionAsync {
            val result = it.where(Item::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }

    fun deleteAll(slug: String) {
        realm.executeTransactionAsync {
            val result = it.where(Item::class.java).equalTo("slug", slug).findAll()
            result.deleteAllFromRealm()
        }
    }

    fun clearKart(){
        realm.executeTransactionAsync {
            val result = it.where(Item::class.java).findAll()
            result.deleteAllFromRealm()
        }
    }

}