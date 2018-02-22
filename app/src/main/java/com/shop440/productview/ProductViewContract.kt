package com.shop440.productview

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.checkout.models.Item
import com.shop440.viewmodel.KartViewModel
import com.shop440.checkout.models.ShopOrders
import com.shop440.dao.models.ProductFeed
import com.shop440.utils.FileCache
import io.realm.RealmResults
import java.io.File

/**
 * Created by mmumene on 09/09/2017.
 */

interface ProductViewContract {

    interface View : BaseView<Presenter> {
        fun showProduct(product: ProductFeed)
        fun cartLoaded(realmResults: RealmResults<Item>?)
        fun categoryNameResolved(category:String)
        fun shopOrder(shopOrders: ShopOrders)
        fun getViewModel() : KartViewModel
    }

    interface Presenter : BasePresenter {
        fun loadCart(activity: ProductViewActivity)
        fun loadData(path: String)
        fun resolveCategory(slug:String)
        fun addToCart(product: ProductFeed)
        fun getShopOrder(shopId:String)
    }
}