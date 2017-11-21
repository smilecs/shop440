package com.shop440.models

/**
 * Created by mmumene on 19/11/2017.
 */
object Feed{
    data class StoreFeed(val slug:String,
                         val shopName:String,
                         val address:String,
                         val imageUrl:String,
                         val description:String)

    data class ProductFeed(val slug:String,
                           val productName:String,
                           val productPrice:Int,
                           val imageUrl:String)
}