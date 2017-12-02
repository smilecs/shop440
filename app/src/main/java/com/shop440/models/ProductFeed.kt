package com.shop440.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by mmumene on 21/11/2017.
 */
data class ProductFeed(val slug:String,
                       @SerializedName("title") val productName:String,
                       @SerializedName("price") val productPrice:Int,
                       @SerializedName("description") val productDesc:String,
                       val category:String,
                       val shop:Store.Store,
                       val tags:List<String>,
                       @SerializedName("City") val city:String,
                       @SerializedName("CitySlug") val citySlug:String,
                       @SerializedName("shop_id") val shopId: String,
                       @SerializedName("geo") val location: Location,
                       val images: List<Image>?):Serializable