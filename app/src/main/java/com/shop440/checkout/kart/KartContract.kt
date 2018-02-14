package com.shop440.checkout.kart

import android.support.v4.app.Fragment
import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.dao.models.ProductFeed
import com.shop440.checkout.models.Item
import com.shop440.checkout.models.ItemForKart
import io.realm.RealmResults

/**
 * Created by mmumene on 03/02/2018.
 */
interface KartContract {

    interface View : BaseView<Presenter> {
        fun onKartLoaded(realmResults: RealmResults<Item>?)
        fun onItemAvailable(item: Item)
        fun onItemDeleted()
        fun getViewModel(): KartViewModel
    }

    interface Presenter : BasePresenter {
        fun loadKart(fragment: Fragment)
        fun addToKart(product: ProductFeed)
        fun addToKart(itemForKart: ItemForKart)
        fun deleteAll(slug:String)
        fun deleteFromKart(id: String)
        fun getItem(id: String)
    }
}