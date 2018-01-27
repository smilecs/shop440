package com.shop440.cart

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by SMILECS on 1/6/18.
 */

open class Order : RealmObject(){
    var phone: String = ""
    var image: String = ""
    var name: String = ""
    var email: String = ""
    var imageUrl: String = ""
    @SerializedName(value = "postalcode") var postalCode: String = ""
    var city: String = ""
    @SerializedName(value = "address1") var address: String = ""
    var company: String = ""
    var province: String = ""
    var total: Double = 0.0
    var shopOrders = RealmList<ShopOrders>()
}