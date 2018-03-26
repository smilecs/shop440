package com.shop440.repository.dao.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable

object Store{
    data class Store(@SerializedName("shop_id") val shopId:String,
                     val title:String,
                     val specialization:String,
                     @SerializedName("Logo") val logo:String,
                     val address : String,
                     val location: String,
                     val phone:String,
                     val description:String) : Serializable
    data class StoreItem(var name:String, var price:String)
}


