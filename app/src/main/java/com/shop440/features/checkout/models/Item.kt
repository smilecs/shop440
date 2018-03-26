package com.shop440.features.checkout.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by SMILECS on 1/7/18.
 */
 open class Item(@SerializedName(value = "total_price") var totalPrice: Double,
                @SerializedName(value = "slug") var slug: String,
                 var shopName:String,
                 var shopSlug:String, @PrimaryKey var id:String,
                 var itemName:String) : RealmObject(){
    constructor() : this(0.0, "", "", "", "", "")
}
