package com.shop440.features.navigation.home.adaptermodel

/**
 * Created by mmumene on 19/11/2017.
 */
abstract class AdapterModel(var title:String, val slug:String){
    abstract fun type() :Int
    abstract fun size():Int
}