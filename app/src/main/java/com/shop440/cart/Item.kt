package com.shop440.cart

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * Created by SMILECS on 1/7/18.
 */
 open class Item(@SerializedName(value = "total_price") var totalPrice: Double,
                @SerializedName(value = "slug") var slug: String, var shopName:String, var shopSlug:String) : RealmObject(){
    constructor() : this(0.0, "", "", "")
}
