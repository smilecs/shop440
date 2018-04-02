package com.shop440.features.checkout.kart

import android.support.v4.app.Fragment
import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.repository.dao.models.Product
import com.shop440.features.checkout.models.Item
import com.shop440.features.checkout.models.ItemForKart
import com.shop440.features.navigation.home.adaptermodel.AdapterModel
import com.shop440.viewmodel.KartViewModel
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

    interface OrderView : BaseView<OrderPresenter>{
        fun onKartLoaded(items:ArrayList<AdapterModel>, total:Double)
        fun getViewModel(): KartViewModel
    }

    interface OrderPresenter : BasePresenter{
        fun loadKart(fragment: Fragment)
    }

    interface Presenter : BasePresenter {
        fun loadKart(fragment: Fragment)
        fun addToKart(product: Product)
        fun addToKart(itemForKart: ItemForKart)
        fun deleteAll(slug:String)
        fun deleteFromKart(id: String)
        fun getItem(id: String)
    }
}