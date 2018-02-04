package com.shop440.kart

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.dao.models.ProductFeed
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
        fun loadKart(activity: BaseKartActivity)
        fun addToKart(product: ProductFeed)
        fun addToKart(itemForKart: ItemForKart)
        fun deleteAll(slug:String)
        fun deleteFromKart(id: String)
        fun getItem(id: String)
    }
}