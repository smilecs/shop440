package com.shop440.features.productview

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.features.checkout.models.Item
import com.shop440.viewmodel.KartViewModel
import com.shop440.features.checkout.models.ShopOrders
import com.shop440.repository.dao.models.Product
import io.realm.RealmResults

/**
 * Created by mmumene on 09/09/2017.
 */

interface ProductViewContract {

    interface View : BaseView<Presenter> {
        fun showProduct(product: Product)
        fun cartLoaded(realmResults: RealmResults<Item>?)
        fun categoryNameResolved(category:String)
        fun shopOrder(shopOrders: ShopOrders)
        fun getViewModel() : KartViewModel
    }

    interface Presenter : BasePresenter {
        fun loadCart(activity: ProductViewActivity)
        fun loadData(path: String)
        fun resolveCategory(slug:String)
        fun addToCart(product: Product)
        fun getShopOrder(shopId:String)
    }
}