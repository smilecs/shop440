package com.shop440.kart

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by SMILECS on 1/7/18.
 */
open class ShopOrders: RealmObject(){
    @SerializedName(value = "order_id") var orderId:String = ""
    @SerializedName(value = "shop_name") var shopName: String = ""
    @PrimaryKey
    @SerializedName(value = "shop_id") var shopId: String = ""
    @SerializedName(value = "shipping") var shipping: Double = 0.0
    @SerializedName(value = "items_cost") var itemCost: Double = 0.0
    @SerializedName(value = "total") var total: Double = 0.0
    var items = RealmList<Item>()
}