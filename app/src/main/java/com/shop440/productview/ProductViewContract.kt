package com.shop440.productview

import com.shop440.BasePresenter
import com.shop440.BaseView
import com.shop440.cart.ShopOrders
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
        fun imageDownloaded(filePath: File)
        fun cartLoaded(realmResults: RealmResults<ShopOrders>?)
        fun categoryNameResolved(category:String)
        fun shopOrder(shopOrders: ShopOrders)
        fun getViewModel() : ProductViewModel
    }

    interface Presenter : BasePresenter {
        fun loadCart(activity: ProductViewActivity)
        fun loadData(path: String)
        fun resolveCategory(slug:String)
        fun addToCart(product: ProductFeed)
        fun downloadImage(imageUrl: String, productName: String, filePath: FileCache)
        fun getShopOrder(shopId:String)
    }
}