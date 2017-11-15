package com.shop440.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

object Store{
    data class Store(var slug:String, var name:String, var spec:String, var logo:String, var desc:String) : Serializable
    data class StoreItem(var name:String, var price:String)
}


